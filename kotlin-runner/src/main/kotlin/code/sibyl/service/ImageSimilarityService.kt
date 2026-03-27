package code.sibyl.service

import code.sibyl.KotlinApplication
import code.sibyl.common.r
import code.sibyl.domain.base.BaseFile
import code.sibyl.dto.BatchEmbeddingRequest
import code.sibyl.dto.ImageEmbeddingRequest
import code.sibyl.dto.ImageSimilaritySearchRequest
import code.sibyl.dto.SimilarImageResult
import code.sibyl.service.sql.PostgresqlService
import lombok.extern.slf4j.Slf4j
import org.opencv.core.*
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.DescriptorMatcher
import org.opencv.features2d.ORB
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty
import java.io.File
import java.time.LocalDateTime
import kotlin.math.max
import kotlin.math.sqrt

/**
 * 图片相似度服务 (纯 Java 实现，依赖 OpenCV)
 * 
 * 技术方案:
 * 1. OpenCV ORB 特征检测 + 描述子提取
 * 2. 特征向量存入 PostgreSQL + pgvector (基于 T_BASE_FILE 表)
 * 3. 查询时用 L2 距离搜索
 */
@Slf4j
@Service
class ImageSimilarityService {

    private val log = LoggerFactory.getLogger(KotlinApplication::class.java)

    companion object {
        init {
            // 加载 OpenCV native 库 (JavaCV/bytedeco)
            try {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
            } catch (e: UnsatisfiedLinkError) {
                error("[OpenCV] Failed to load native library from system path, will retry on first use")
            }
        }
        
        const val ORB_DESCRIPTOR_DIM = 256
        const val ORB_MAX_KEYPOINTS = 500
    }

    @Autowired
    @Qualifier("sibyl-postgresql")
    private val r2dbcEntityTemplate: R2dbcEntityTemplate? = null

    /**
     * 提取图片的 ORB 特征向量
     */
    fun extractOrbFeatures(imagePath: String): OrbFeatureResult {
        val file = File(imagePath)
        if (!file.exists()) {
            throw IllegalArgumentException("Image file not found: $imagePath")
        }

        val image = Imgcodecs.imread(imagePath)
        if (image.empty()) {
            throw IllegalArgumentException("Failed to read image: $imagePath")
        }

        val grayImage = Mat()
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY)

        // 缩放图像以提高处理速度 (最长边缩放到 800)
        val maxDim = 800.0
        val scale = if (image.width() > image.height()) {
            maxDim / image.width()
        } else {
            maxDim / image.height()
        }

        val result = if (scale < 1.0) {
            val resized = Mat()
            Imgproc.resize(grayImage, resized, Size(image.width() * scale, image.height() * scale))
            grayImage.release()
            extractOrbFromGray(resized)
        } else {
            extractOrbFromGray(grayImage)
        }

        image.release()
        return result
    }

    private fun extractOrbFromGray(grayImage: Mat): OrbFeatureResult {
        val orb = ORB.create(
            ORB_MAX_KEYPOINTS,
            1.2f,
            8,
            31,
            0,
            2,
            ORB.HARRIS_SCORE,
            31,
            20
        )

        val keypoints = MatOfKeyPoint()
        val descriptors = Mat()
        orb.detectAndCompute(grayImage, Mat(), keypoints, descriptors)

        // 将 ORB 描述子矩阵展平为浮点数列表
        val featureVector = matToFloatList(descriptors)

        // 计算均值指纹 (简化版向量，用于快速比较)
        val fingerprint = if (descriptors.rows() > 0 && descriptors.cols() > 0) {
            (0 until descriptors.cols()).map { col ->
                descriptors.get(0, col).average()
            }
        } else {
            emptyList()
        }

        grayImage.release()

        return OrbFeatureResult(
            keypointCount = keypoints.rows(),
            descriptorDim = descriptors.cols(),
            featureVector = featureVector,
            fingerprint = fingerprint
        )
    }

    private fun matToFloatList(descriptors: Mat): List<Double> {
        if (descriptors.empty()) return emptyList()

        val floatList = mutableListOf<Double>()
        for (i in 0 until descriptors.rows()) {
            for (j in 0 until descriptors.cols()) {
                floatList.add(descriptors.get(i, j)[0])
            }
        }
        return floatList
    }

    /**
     * 匹配两张图片，返回相似度分数 (0.0-1.0)
     */
    fun matchImages(imagePath1: String, imagePath2: String): Double {
        val img1 = Imgcodecs.imread(imagePath1, Imgcodecs.IMREAD_GRAYSCALE)
        val img2 = Imgcodecs.imread(imagePath2, Imgcodecs.IMREAD_GRAYSCALE)

        if (img1.empty() || img2.empty()) {
            return 0.0
        }

        val orb = ORB.create(ORB_MAX_KEYPOINTS)
        val kp1 = MatOfKeyPoint()
        val kp2 = MatOfKeyPoint()
        val desc1 = Mat()
        val desc2 = Mat()

        orb.detectAndCompute(img1, Mat(), kp1, desc1)
        orb.detectAndCompute(img2, Mat(), kp2, desc2)

        img1.release()
        img2.release()

        val matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING)
        val matches = MatOfDMatch()
        if (desc1.rows() > 0 && desc2.rows() > 0) {
            matcher.match(desc1, desc2, matches)
        }

        desc1.release()
        desc2.release()

        val matchList = matches.toList()
        matches.release()

        if (matchList.isEmpty()) return 0.0

        var maxDist = 0.0
        var minDist = Double.MAX_VALUE
        for (dMatch in matchList) {
            val dist = dMatch.distance.toDouble()
            if (dist < minDist) minDist = dist
            if (dist > maxDist) maxDist = dist
        }

        // 筛选好的匹配 (Lowe's ratio test)
        val threshold = max(2 * minDist, 0.02)
        val goodMatches = matchList.filter { it.distance <= threshold }

        return goodMatches.size.toDouble() / matchList.size.coerceAtLeast(1)
    }

    /**
     * 单张图片入库
     */
    fun embedImage(request: ImageEmbeddingRequest): Mono<BaseFile> {
        return Mono.fromCallable {
            val imagePath = request.imagePath
                ?: throw IllegalArgumentException("imagePath is required")

            val file = File(imagePath)
            if (!file.exists()) {
                throw IllegalArgumentException("Image file not found: $imagePath")
            }

            val sha256 = r.computeFileSHA256(file)
            val features = extractOrbFeatures(imagePath)

            // 构建 pgvector 格式的向量字符串
            val vector = if (features.featureVector.isNotEmpty()) {
                "[" + features.featureVector.joinToString(",") + "]"
            } else {
                "[" + features.fingerprint.joinToString(",") { it.toString() } + "]"
            }

            BaseFile().apply {
                this.absolutePath = imagePath
                this.fileName = file.name
                this.sha256 = sha256
                this.imageDescription = "ORB:${features.keypointCount}keypoints"
                this.vectorDim = if (features.featureVector.isNotEmpty()) features.featureVector.size else features.fingerprint.size
                this.embeddingVector = vector
                this.groupName = request.groupName
                this.similarityStatus = "completed"
                this.createTime = LocalDateTime.now()
                this.updateTime = LocalDateTime.now()
            }
        }.flatMap { baseFile ->
            insertOrUpdateBaseFile(baseFile)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    /**
     * 批量扫描目录并入库
     */
    fun batchEmbedFromDirectory(request: BatchEmbeddingRequest): Flux<BaseFile> {
        val dirPath = request.directoryPath
            ?: throw IllegalArgumentException("directoryPath is required")

        val dir = File(dirPath)
        if (!dir.exists() || !dir.isDirectory) {
            throw IllegalArgumentException("Invalid directory: $dirPath")
        }

        val extensions = request.extensions.map { it.lowercase() }.toSet()

        val imageFiles = r.getAllFiles(dirPath)
            .filter { path ->
                val ext = path.substringAfterLast('.', "").lowercase()
                extensions.contains(ext)
            }
            .collectList()
            .block() ?: emptyList()

        log.info("[ImageSimilarity] Found ${imageFiles.size} images in $dirPath")

        return Flux.fromIterable(imageFiles)
            .flatMap { imagePath ->
                val embedRequest = ImageEmbeddingRequest().apply {
                    this.imagePath = imagePath
                    this.groupName = request.groupName
                }
                embedImage(embedRequest)
                    .onErrorResume { e ->
                        log.error("[ImageSimilarity] Failed to embed: $imagePath", e)
                        Mono.empty()
                    }
            }
    }

    /**
     * 搜索相似图片 (按 BaseFile ID 查询)
     */
    fun searchSimilarImagesById(request: ImageSimilaritySearchRequest): Flux<SimilarImageResult> {
        return Mono.fromCallable {
            val id = request.id
                ?: throw IllegalArgumentException("id is required")

            // 从 BaseFile 表中查找记录
            val template = PostgresqlService.getBean().template()
            val criteria = Criteria.where("id").`is`(id)
            val query = Query.query(criteria)
            
            val baseFile = template.select(query, BaseFile::class.java).next().block()
                ?: throw IllegalArgumentException("BaseFile not found for id: $id")

            val filePath = baseFile.absolutePath
                ?: throw IllegalArgumentException("absolutePath is null for id: $id")

            // 排除自身
            val excludes = request.excludePaths.orEmpty().toMutableList()
            excludes.add(filePath)
            request.excludePaths = excludes

            extractOrbFeatures(filePath)
        }.flatMapMany { targetFeatures ->
            searchSimilarInDatabase(targetFeatures.featureVector, request)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    /**
     * 按 BaseFile ID 快速匹配两张图片
     */
    fun matchTwoImagesById(id1: Long, id2: Long): Mono<Map<String, Any>> {
        return Mono.fromCallable {
            val template = PostgresqlService.getBean().template()

            val baseFile1 = template.select(Query.query(Criteria.where("id").`is`(id1)), BaseFile::class.java).next().block()
                ?: throw IllegalArgumentException("BaseFile not found for id1: $id1")
            val baseFile2 = template.select(Query.query(Criteria.where("id").`is`(id2)), BaseFile::class.java).next().block()
                ?: throw IllegalArgumentException("BaseFile not found for id2: $id2")

            val path1 = baseFile1.absolutePath ?: throw IllegalArgumentException("absolutePath is null for id1: $id1")
            val path2 = baseFile2.absolutePath ?: throw IllegalArgumentException("absolutePath is null for id2: $id2")

            val similarity = matchImages(path1, path2)

            val result = mutableMapOf<String, Any>()
            result["id1"] = id1
            result["id2"] = id2
            result["path1"] = path1
            result["path2"] = path2
            result["similarity"] = "%.4f".format(similarity)
            result as Map<String, Any>
        }.subscribeOn(Schedulers.boundedElastic())
    }

    /**
     * 在数据库中搜索相似向量
     */
    private fun searchSimilarInDatabase(
        targetVector: List<Double>,
        request: ImageSimilaritySearchRequest
    ): Flux<SimilarImageResult> {
        if (targetVector.isEmpty()) {
            return Flux.empty()
        }

        val targetVectorStr = targetVector.joinToString(",", "[", "]")
        val excludeClause = request.excludePaths?.takeIf { it.isNotEmpty() }
            ?.joinToString("','", "AND absolute_path NOT IN ('", "')") { it.replace("'", "''") }
            ?: ""
        val groupClause = request.groupName?.let { "AND group_name = '$it'" } ?: ""

        val sql = """
            SELECT id, absolute_path, sha256, image_description, 
                   (embedding_vector::vector <-> '$targetVectorStr'::vector) as distance
            FROM t_base_file 
            WHERE similarity_status = 'completed'
            AND embedding_vector IS NOT NULL
            $groupClause
            $excludeClause
            ORDER BY embedding_vector::vector <-> '$targetVectorStr'::vector
            LIMIT ${request.limit}
        """.trimIndent()

        val template = PostgresqlService.getBean().template()
        val databaseClient = template.databaseClient

        return databaseClient.sql(sql)
            .map { row ->
                val distance = row.get("distance", Double::class.javaObjectType) ?: 0.0
                // L2 距离转相似度 (指数衰减)
                val similarity = 1.0 / (1.0 + distance)

                SimilarImageResult().apply {
                    this.embeddingId = row.get("id", Long::class.javaObjectType)
                    this.filePath = row.get("absolute_path", String::class.java)
                    this.fileSha256 = row.get("sha256", String::class.java)
                    this.similarity = similarity
                    this.imageDescription = row.get("image_description", String::class.java)
                }
            }
            .all()
            .filter { it.similarity >= request.threshold }
    }

    /**
     * 插入或更新 BaseFile 记录 (按 SHA256 去重)
     */
    private fun insertOrUpdateBaseFile(baseFile: BaseFile): Mono<BaseFile> {
        val sha256 = baseFile.sha256
            ?: throw IllegalArgumentException("sha256 is required")

        val criteria = Criteria.where("sha256").`is`(sha256)
        val query = Query.query(criteria)

        return r2dbcEntityTemplate!!.select(query, BaseFile::class.java)
            .next()
            .flatMap { existing ->
                // 更新已有记录
                existing.embeddingVector = baseFile.embeddingVector
                existing.vectorDim = baseFile.vectorDim
                existing.imageDescription = baseFile.imageDescription
                existing.groupName = baseFile.groupName
                existing.similarityStatus = "completed"
                existing.similarityErrorMsg = null
                existing.updateTime = LocalDateTime.now()

                r2dbcEntityTemplate.update(existing).thenReturn(existing)
            }
            .switchIfEmpty {
                // 插入新记录
                baseFile.isDeleted = "0"
                r2dbcEntityTemplate.insert(baseFile)
            }
    }

    /**
     * 快速测试: 匹配两张图片的相似度 (不查数据库)
     */
    fun matchTwoImages(imagePath1: String, imagePath2: String): Double {
        return matchImages(imagePath1, imagePath2)
    }
}

/**
 * ORB 特征提取结果
 */
data class OrbFeatureResult(
    val keypointCount: Int,
    val descriptorDim: Int,
    val featureVector: List<Double>,
    val fingerprint: List<Double>
)

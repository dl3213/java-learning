package code.sibyl.service

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OnnxValue
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import code.sibyl.common.r
import code.sibyl.domain.base.BaseFile
import code.sibyl.dto.BatchEmbeddingRequest
import code.sibyl.dto.ImageEmbeddingRequest
import code.sibyl.dto.ImageSimilaritySearchRequest
import code.sibyl.dto.SimilarImageResult
import code.sibyl.service.sql.PostgresqlService
import lombok.extern.slf4j.Slf4j
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty
import java.io.File
import java.nio.FloatBuffer
import java.time.LocalDateTime
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * CLIP 图片相似度服务 (纯 Java 实现，依赖 ONNX Runtime)
 * 
 * 使用 CLIP ViT-B/32 模型提取图片特征向量 (512维)
 * ONNX 模型文件: D:\4code\4java\workspace\java-learning\models\clip-vit-b32-vision.onnx
 */
@Slf4j
@Service
class ClipImageSimilarityService {

    companion object {
        init {
            // 加载 OpenCV native 库
            try {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
            } catch (e: UnsatisfiedLinkError) {
                error("[OpenCV] Failed to load native library")
            }
        }

        // CLIP ViT-B/32 输入尺寸
        const val CLIP_IMAGE_SIZE = 224
        // CLIP ViT-B/32 输出特征维度
        const val CLIP_FEATURE_DIM = 512
        // CLIP mean/std 归一化参数
        val CLIP_MEAN = floatArrayOf(0.48145466f, 0.4578275f, 0.40821073f)
        val CLIP_STD = floatArrayOf(0.26862954f, 0.26130258f, 0.27577711f)
    }

    @Value("\${image.similarity.clip.model-path:D:/4code/4java/workspace/java-learning/models/clip-vit-b32-vision.onnx}")
    var modelPath: String = "D:/4code/4java/workspace/java-learning/models/clip-vit-b32-vision.onnx"

    @Value("\${image.similarity.clip.batch-size:8}")
    var batchSize: Int = 8

    // ONNX Runtime environment & session (延迟初始化)
    private var ortEnvironment: OrtEnvironment? = null
    private var ortSession: OrtSession? = null

    /**
     * 获取 ONNX Runtime session (单例)
     */
    private fun getOrtSession(): OrtSession {
        if (ortSession == null) {
            synchronized(this) {
                if (ortSession == null) {
                    ortEnvironment = OrtEnvironment.getEnvironment()
                    val sessionOptions = OrtSession.SessionOptions()
                    sessionOptions.setInterOpNumThreads(4)
                    sessionOptions.setIntraOpNumThreads(4)
                    ortSession = ortEnvironment!!.createSession(modelPath, sessionOptions)
                    println("[CLIP] ONNX Runtime session initialized, model: {}" +  modelPath)
                }
            }
        }
        return ortSession!!
    }

    /**
     * 提取图片的 CLIP 特征向量 (512维)
     * 
     * @param imagePath 图片路径
     * @return 512维特征向量
     */
    fun extractClipFeatures(imagePath: String): List<Double> {
        val file = File(imagePath)
        if (!file.exists()) {
            throw IllegalArgumentException("Image file not found: $imagePath")
        }

        // 1. 读取并预处理图片 (OpenCV)
        val mat = Imgcodecs.imread(imagePath)
        if (mat.empty()) {
            throw IllegalArgumentException("Failed to read image: $imagePath")
        }

        // 2. 缩放到 CLIP 输入尺寸 (224x224) 并转换为 RGB
        val resized = Mat()
        Imgproc.resize(mat, resized, Size(CLIP_IMAGE_SIZE.toDouble(), CLIP_IMAGE_SIZE.toDouble()))
        mat.release()

        // 3. BGR -> RGB
        val rgb = Mat()
        Imgproc.cvtColor(resized, rgb, Imgproc.COLOR_BGR2RGB)
        resized.release()

        // 4. 归一化到 [0, 1] 并应用 CLIP mean/std
        val floatValues = FloatArray(3 * CLIP_IMAGE_SIZE * CLIP_IMAGE_SIZE)
        for (i in 0 until CLIP_IMAGE_SIZE) {
            for (j in 0 until CLIP_IMAGE_SIZE) {
                val pixel = rgb.get(i, j)
                val idx = (i * CLIP_IMAGE_SIZE + j)
                floatValues[idx] = (pixel[0].toFloat() / 255.0f - CLIP_MEAN[0]) / CLIP_STD[0]
                floatValues[CLIP_IMAGE_SIZE * CLIP_IMAGE_SIZE + idx] = (pixel[1].toFloat() / 255.0f - CLIP_MEAN[1]) / CLIP_STD[1]
                floatValues[2 * CLIP_IMAGE_SIZE * CLIP_IMAGE_SIZE + idx] = (pixel[2].toFloat() / 255.0f - CLIP_MEAN[2]) / CLIP_STD[2]
            }
        }
        rgb.release()

        // 5. 转换为 NCHW 格式 (batch=1, channels=3, height=224, width=224)
        val nchwValues = FloatArray(1 * 3 * CLIP_IMAGE_SIZE * CLIP_IMAGE_SIZE)
        // NCHW: channel 0 = all R values, channel 1 = all G, channel 2 = all B
        for (c in 0 until 3) {
            for (i in 0 until CLIP_IMAGE_SIZE) {
                for (j in 0 until CLIP_IMAGE_SIZE) {
                    val srcIdx = c * CLIP_IMAGE_SIZE * CLIP_IMAGE_SIZE + i * CLIP_IMAGE_SIZE + j
                    val dstIdx = c * CLIP_IMAGE_SIZE * CLIP_IMAGE_SIZE + i * CLIP_IMAGE_SIZE + j
                    nchwValues[dstIdx] = floatValues[srcIdx]
                }
            }
        }

        // 6. 通过 ONNX Runtime 推理
        val session = getOrtSession()
        val inputName = session.inputNames.first()
        val outputName = session.outputNames.first()

        val inputTensor = OnnxTensor.createTensor(
            ortEnvironment!!,
            FloatBuffer.wrap(nchwValues),
            longArrayOf(1, 3, CLIP_IMAGE_SIZE.toLong(), CLIP_IMAGE_SIZE.toLong())
        )

        val outputs = session.run(mapOf(inputName to inputTensor), session.outputNames)
        @Suppress("UNCHECKED_CAST")
        val outputTensor = (outputs[outputName] as java.util.Optional<OnnxValue>).get() as OnnxTensor
        val rawFeatures = outputTensor.floatBuffer.array()

        // 7. L2 归一化 (CLIP 的特征默认是 L2 归一化的)
        var norm = 0.0
        for (v in rawFeatures) {
            norm += v.toDouble() * v.toDouble()
        }
        norm = kotlin.math.sqrt(norm)

        inputTensor.close()
        outputTensor.close()

        return rawFeatures.map { (it.toDouble() / norm) }
    }

    /**
     * 计算两张图片的 CLIP 相似度
     */
    fun computeSimilarity(imagePath1: String, imagePath2: String): Double {
        val features1 = extractClipFeatures(imagePath1)
        val features2 = extractClipFeatures(imagePath2)

        // 余弦相似度 (由于特征已 L2 归一化，直接点积即可)
        var dot = 0.0
        for (i in features1.indices) {
            dot += features1[i] * features2[i]
        }
        return (dot + 1.0) / 2.0  // 转换到 [0, 1]
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
            val features = extractClipFeatures(imagePath)

            // 构建 pgvector 格式的向量字符串
            val vectorStr = "[" + features.joinToString(",") + "]"

            BaseFile().apply {
                this.absolutePath = imagePath
                this.fileName = file.name
                this.sha256 = sha256
                this.imageDescription = "CLIP:${CLIP_FEATURE_DIM}dim"
                this.vectorDim = CLIP_FEATURE_DIM
                this.embeddingVector = vectorStr
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

        println("[CLIP] Found ${imageFiles.size} images in $dirPath")

        return Flux.fromIterable(imageFiles)
            .flatMap { imagePath ->
                val embedRequest = ImageEmbeddingRequest().apply {
                    this.imagePath = imagePath
                    this.groupName = request.groupName
                }
                embedImage(embedRequest)
                    .onErrorResume { e ->
                        println("[CLIP] Failed to embed: $imagePath" + e)
                        Mono.empty()
                    }
            }
    }

    /**
     * 搜索相似图片 (按 BaseFile ID)
     */
    fun searchSimilarImagesById(request: ImageSimilaritySearchRequest): Flux<SimilarImageResult> {
        return Mono.fromCallable {
            val id = request.id
                ?: throw IllegalArgumentException("id is required")

            val template = PostgresqlService.getBean().template()
            val baseFile = template.select(Query.query(Criteria.where("id").`is`(id)), BaseFile::class.java).next().block()
                ?: throw IllegalArgumentException("BaseFile not found for id: $id")

            val filePath = baseFile.absolutePath
                ?: throw IllegalArgumentException("absolutePath is null for id: $id")

            // 排除自身
            val excludes = request.excludePaths.orEmpty().toMutableList()
            excludes.add(filePath)
            request.excludePaths = excludes

            extractClipFeatures(filePath)
        }.flatMapMany { targetFeatures ->
            searchSimilarInDatabase(targetFeatures, request)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    /**
     * 在数据库中搜索相似向量 (余弦相似度)
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

        // 使用余弦相似度 (<=>) 进行搜索
        val sql = """
            SELECT id, absolute_path, sha256, image_description, 
                   (embedding_vector::vector <=> '$targetVectorStr'::vector) as distance
            FROM t_base_file 
            WHERE similarity_status = 'completed'
            AND embedding_vector IS NOT NULL
            AND vector_dim = $CLIP_FEATURE_DIM
            $groupClause
            $excludeClause
            ORDER BY embedding_vector::vector <=> '$targetVectorStr'::vector
            LIMIT ${request.limit}
        """.trimIndent()

        val template = PostgresqlService.getBean().template()
        val databaseClient = template.databaseClient

        return databaseClient.sql(sql)
            .map { row ->
                val distance = row.get("distance", Double::class.javaObjectType) ?: 0.0
                // 余弦距离转换为相似度 (0.05 distance ≈ 0.95 similarity)
                val similarity = 1.0 - distance

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

        return PostgresqlService.getBean().template().select(query, BaseFile::class.java)
            .next()
            .flatMap { existing ->
                existing.embeddingVector = baseFile.embeddingVector
                existing.vectorDim = baseFile.vectorDim
                existing.imageDescription = baseFile.imageDescription
                existing.groupName = baseFile.groupName
                existing.similarityStatus = "completed"
                existing.similarityErrorMsg = null
                existing.updateTime = LocalDateTime.now()
                PostgresqlService.getBean().template().update(existing).thenReturn(existing)
            }
            .switchIfEmpty {
                baseFile.isDeleted = "0"
                PostgresqlService.getBean().template().insert(baseFile)
            }
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

            val similarity = computeSimilarity(path1, path2)

            mapOf<String, Any>(
                "id1" to id1,
                "id2" to id2,
                "path1" to path1,
                "path2" to path2,
                "similarity" to "%.4f".format(similarity)
            )
        }.subscribeOn(Schedulers.boundedElastic())
    }
}

package code.sibyl.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import lombok.Data
import java.time.LocalDateTime

/**
 * 图片相似度搜索请求
 */
@Data
class ImageSimilaritySearchRequest {
    /** 目标图片的 BaseFile ID */
    var id: Long? = null

    /** 匹配数量限制 */
    var limit: Int = 10

    /** 相似度阈值 (0.0-1.0)，低于此值的过滤掉 */
    var threshold: Double = 0.3

    /** 在指定分组内搜索 */
    var groupName: String? = null

    /** 排除的图片路径 (比如查询的源图本身) */
    var excludePaths: List<String>? = null
}

/**
 * 相似图片结果项
 */
@Data
class SimilarImageResult {
    var rank: Int = 0
    var filePath: String? = null
    var fileSha256: String? = null
    var similarity: Double = 0.0
    var imageDescription: String? = null

    @JsonSerialize(using = ToStringSerializer::class)
    var embeddingId: Long? = null
}

/**
 * 图片向量入库请求
 */
@Data
class ImageEmbeddingRequest {
    /** 图片路径 (文件系统路径) */
    var imagePath: String? = null

    /** 所属分组 */
    var groupName: String? = null
}

/**
 * 批量入库请求
 */
@Data
class BatchEmbeddingRequest {
    /** 目录路径，扫描此目录下所有图片 */
    var directoryPath: String? = null

    /** 图片文件扩展名过滤 */
    var extensions: List<String> = listOf("jpg", "jpeg", "png", "webp", "bmp")

    /** 所属分组 */
    var groupName: String? = null

    /** 是否跳过已有记录 (按SHA256去重) */
    var skipExisting: Boolean = true
}

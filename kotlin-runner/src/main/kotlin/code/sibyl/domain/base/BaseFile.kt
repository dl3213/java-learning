package code.sibyl.domain.base

import code.sibyl.common.r
import code.sibyl.service.cdc.LocalDateTimeDeserializer
import com.alibaba.fastjson2.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import jakarta.persistence.*
import lombok.Data
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Data
@Entity
@Table(name = "T_BASE_FILE")
@org.springframework.data.relational.core.mapping.Table(name = "T_BASE_FILE")
open class BaseFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id
    @JsonSerialize(using = ToStringSerializer::class)
    open var id: Long? = null

    open var fileName: String? = null
    open var realName: String? = null

    open var type: String? = null

    open var absolutePath: String? = null
    open var relativePath: String? = null

    open var size: Long? = null
    @org.springframework.data.annotation.Transient
    @Transient
    open var sizeDescription: String? = null

    open var sha256: String? = null

    open var suffix: String? = null

    open var serialNumber: String? = null

    open var code: String? = null
    open var width: Int? = null
    open var height: Int? = null
    open var thumbnail: String? = null

    open var isDeleted: String? = null

    @DateTimeFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    @JsonFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    @JSONField(deserializeUsing = LocalDateTimeDeserializer::class)
    open var createTime: LocalDateTime? = null

    open var createId: Long? = null

    @DateTimeFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    @JsonFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    @JSONField(deserializeUsing = LocalDateTimeDeserializer::class)
    open var updateTime: LocalDateTime? = null

    open var updateId: Long? = null
    open var clickCount: Int? = 0;
    open var m3u8Path: String? = null;

    open var videoDurationSecond : Double? = null

    @org.springframework.data.annotation.Transient
    @Transient
    open var heartCount: Int? = 0;
    @org.springframework.data.annotation.Transient
    @Transient
    open var isBigFile: String = "0";

    @org.springframework.data.annotation.Transient
    @Transient
    open var heartByCurrentUserCount: Int? = 0;
    @org.springframework.data.annotation.Transient
    @Transient
    open var prevUrl: String = r.staticFileBasePath.replace("**", "");


    @org.springframework.data.annotation.Transient
    @Transient
    open var htmlTemplate: String = "";

    @org.springframework.data.annotation.Transient
    @Transient
    open var gallery: String? = "";

    // ===== 图片相似度向量相关字段 =====

    /** ORB 向量维度 (固定256) */
    open var vectorDim: Int? = null

    /** pgvector 格式的向量字符串 '[0.1,0.2,...]' */
    open var embeddingVector: String? = null

    /** 图片描述 (ORB特征点数量等) */
    open var imageDescription: String? = null

    /** 所属分组/标签 */
    open var groupName: String? = null

    /** 处理状态: pending/processing/completed/failed */
    open var similarityStatus: String? = null

    /** 相似度处理失败时的错误信息 */
    open var similarityErrorMsg: String? = null

    // ===== 文件标签 =====
    @org.springframework.data.annotation.Transient
    @Transient
    open var tags: List<Tag>? = null

}
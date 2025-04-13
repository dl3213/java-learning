package code.sibyl.domain.base

import code.sibyl.common.r
import com.alibaba.fastjson2.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import jakarta.persistence.*
import lombok.Data
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.function.Function

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
    open var createTime: LocalDateTime? = null

    open var createId: Long? = null

    @DateTimeFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    @JsonFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    open var updateTime: LocalDateTime? = null

    open var updateId: Long? = null

}
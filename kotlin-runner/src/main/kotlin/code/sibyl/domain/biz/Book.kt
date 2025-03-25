package code.sibyl.domain.biz

import code.sibyl.common.r
import com.alibaba.fastjson2.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import jakarta.persistence.*
import lombok.Data
import org.springframework.data.relational.core.mapping.Table
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Data
@Entity
@Table(name = "T_BIZ_BOOK")
open class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    @org.springframework.data.annotation.Id
    @JsonSerialize(using = ToStringSerializer::class)
    open var id: Long? = null

    open var name: String? = null

    open var type: String? = null

    open var absolutePath: String? = null

    open var relativePath: String? = null

    open var pageNum: Long? = null

    open var serialNumber: String? = null

    open var code: String? = null
    open var description: String? = null
    open var isDeleted: String? = null

    @DateTimeFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    @JsonFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    open var createTime: LocalDateTime? = null

    @Column(name = "CREATE_ID")
    open var createId: Long? = null

    @DateTimeFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    @JsonFormat(pattern = r.yyyy_MM_dd_HH_mm_ss_SSSSSS)
    open var updateTime: LocalDateTime? = null

    open var updateId: Long? = null
}
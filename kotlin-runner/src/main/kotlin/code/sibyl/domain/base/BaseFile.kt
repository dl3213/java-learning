package code.sibyl.domain.base

import code.sibyl.common.r
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Entity
@Table(name = "T_BASE_FILE")
@org.springframework.data.relational.core.mapping.Table(name = "T_BASE_FILE")
open class BaseFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    @org.springframework.data.annotation.Id
    open var id: Long? = null

    @Column(name = "FILE_NAME")
    open var fileName: String? = null
    open var realName: String? = null

    @Column(name = "TYPE", length = 64)
    open var type: String? = null

    @Column(name = "ABSOLUTE_PATH", length = 1024)
    open var absolutePath: String? = null
    open var relativePath: String? = null

    @Column(name = "SIZE", length = 64)
    open var size: Long? = null

    @Column(name = "SHA256")
    open var sha256: String? = null

    @Column(name = "SUFFIX", length = 64)
    open var suffix: String? = null

    @Column(name = "SERIAL_NUMBER")
    open var serialNumber: String? = null

    @Column(name = "CODE", length = 64)
    open var code: String? = null
    open var width: Int? = null
    open var height: Int? = null
    open var thumbnail: String? = null

    @Column(name = "IS_DELETED", nullable = false, length = 2)
    open var isDeleted: String? = null

    @DateTimeFormat(pattern = r.yyyy_MM_dd_HH_mm_ss)
    @JsonFormat(pattern = r.yyyy_MM_dd_HH_mm_ss)
    @Column(name = "CREATE_TIME")
    open var createTime: LocalDateTime? = null

    @Column(name = "CREATE_ID")
    open var createId: Long? = null

    @DateTimeFormat(pattern = r.yyyy_MM_dd_HH_mm_ss)
    @JsonFormat(pattern = r.yyyy_MM_dd_HH_mm_ss)
    @Column(name = "UPDATE_TIME")
    open var updateTime: LocalDateTime? = null

    @Column(name = "UPDATE_ID")
    open var updateId: Long? = null

}
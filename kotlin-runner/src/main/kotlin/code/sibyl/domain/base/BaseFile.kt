package code.sibyl.domain.base

import jakarta.persistence.*
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

    @Column(name = "TYPE", length = 64)
    open var type: String? = null

    @Column(name = "ABSOLUTE_PATH", length = 1024)
    open var absolutePath: String? = null

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

    @Column(name = "IS_DELETED", nullable = false, length = 2)
    open var isDeleted: String? = null

    @Column(name = "CREATE_TIME")
    open var createTime: LocalDateTime? = null

    @Column(name = "CREATE_ID")
    open var createId: Long? = null

    @Column(name = "UPDATE_TIME")
    open var updateTime: LocalDateTime? = null

    @Column(name = "UPDATE_ID")
    open var updateId: Long? = null
}
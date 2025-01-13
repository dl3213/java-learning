package code.sibyl.domain.biz

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "T_BIZ_BOOK")
open class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    open var id: Long? = null

    @Column(name = "name")
    open var name: String? = null

    @Column(name = "TYPE", length = 64)
    open var type: String? = null

    @Column(name = "ABSOLUTE_PATH", length = 1024)
    open var absolutePath: String? = null

    @Column(name = "relative_path", length = 1024)
    open var relativePath: String? = null

    @Lob
    @Column(name = "page_num")
    open var pageNum: Long? = null

    @Column(name = "SERIAL_NUMBER")
    open var serialNumber: String? = null

    @Column(name = "CODE", length = 64)
    open var code: String? = null

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
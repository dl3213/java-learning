package code.sibyl.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.io.Serializable
import java.time.LocalDateTime

open class BaseEntity : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @org.springframework.data.annotation.Id
    @JsonSerialize(using = ToStringSerializer::class)
    open var id: Long? = null

    open var isDeleted: String = "0"

    open var createId: Long? = null
    open var createTime: LocalDateTime? = null
    open var updateId: Long? = null
    open var updateTime: LocalDateTime? = null
}

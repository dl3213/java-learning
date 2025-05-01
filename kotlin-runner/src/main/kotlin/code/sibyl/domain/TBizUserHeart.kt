package code.sibyl.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.Data
import java.time.Instant
import java.time.LocalDateTime

@Data
@Entity
@Table(name = "t_biz_user_heart")
open class TBizUserHeart {
    @Id
    @Column(name = "id", nullable = false)
    @org.springframework.data.annotation.Id
    open var id: Long? = null

    @Column(name = "user_id")
    open var userId: Long? = null

    @Column(name = "entity_id")
    open var entityId: Long? = null

    @Column(name = "entity_type")
    open var entityType: String? = null

    @Column(name = "is_deleted", nullable = false, length = 1)
    open var isDeleted: String? = null

    @Column(name = "create_time")
    open var createTime: LocalDateTime? = null

    @Column(name = "update_time")
    open var updateTime: LocalDateTime? = null
}
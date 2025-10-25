package code.sibyl.domain.base

import code.sibyl.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "t_base_tag")
@org.springframework.data.relational.core.mapping.Table("t_base_tag")
open class Tag : BaseEntity() {

    @Column(name = "name", length = 1024)
    open var name: String? = null

    @Column(name = "entity_id")
    open var entityId: Long? = null

    @Column(name = "entity_type", length = 32)
    open var entityType: String? = null

}
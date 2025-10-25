package code.sibyl.domain.base

import code.sibyl.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import lombok.Data

@Data
@Entity
@Table(name = "t_base_box")
@org.springframework.data.relational.core.mapping.Table("t_base_box")
open class Box : BaseEntity() {

    open var key: String? = null

    open var value: String? = null

    open var code: String? = null

    open var remark: String? = null
    open var description: String? = null
    open var isEncrypted: String? = "0"
}
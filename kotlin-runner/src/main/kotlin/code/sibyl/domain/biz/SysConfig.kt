package code.sibyl.domain.biz

import jakarta.persistence.*
import lombok.Data
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

/**
 * 系统键值配置
 */
@Data
@Entity
@Table(name = "t_sys_config")
open class SysConfig : code.sibyl.domain.BaseEntity() {

    @Column(name = "key")
    var key: String? = null

    @Column(name = "value")
    var value: String? = null

    @Column(name = "type")
    var type: String? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "description")
    var description: String? = null

    @Column(name = "is_system")
    var isSystem: Boolean? = null
}

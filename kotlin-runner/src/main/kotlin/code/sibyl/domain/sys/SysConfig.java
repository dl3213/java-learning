package code.sibyl.domain.sys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * T_SYS_CONFIG
 */

@Entity
@Table("T_SYS_CONFIG")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @org.springframework.data.annotation.Id
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "DESC", nullable = false)
    private String desc;

    @Column(name = "VERSION")
    private Long version;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "create_id")
    private Long createId;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

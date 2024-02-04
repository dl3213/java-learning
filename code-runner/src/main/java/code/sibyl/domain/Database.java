package code.sibyl.domain;

import code.sibyl.dto.DatabaseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Entity
@Table("T_DATABASE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Database {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(64) COMMENT '主键'", nullable = false)
    private Long id;
    @Column(name = "name", columnDefinition = "varchar(64) COMMENT 'type'", nullable = true)
    private String name;
    @Column(name = "type", columnDefinition = "varchar(64) COMMENT 'type'", nullable = true)
    private String type;
    @Version
    @Column(name = "version", columnDefinition = "int COMMENT 'version'", nullable = true)
    private Integer version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    public Database(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.version = 0;
        this.createTime = LocalDateTime.now();
    }

    public Database(DatabaseDTO dto) {
        this.setName(dto.getName());
        this.setType(dto.getType());
        this.setVersion(0);
        this.setCreateTime(LocalDateTime.now());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

package code.sibyl.domain.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 数据库
 */

@Entity
@Table("T_BASE_DATABASE")
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Database {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @org.springframework.data.annotation.Id
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;

    @Column(name = "host")
    private String host;
    @Column(name = "port")
    private String port;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "database")
    private String database;

    @org.springframework.data.annotation.Transient
//    @Column(name = "desc")\
    private String desc;

    @Version
    @Column(name = "version")
    private Integer version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "create_id")
    private Long createId;

}

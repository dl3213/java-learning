package code.sibyl.domain.database;

import code.sibyl.aop.Header;
import io.r2dbc.spi.ConnectionFactory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.r2dbc.core.DatabaseClient;

import java.io.Serializable;
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
public class Database implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @org.springframework.data.annotation.Id
    @Header
    private Long id;

    @Column(name = "name")
    @Header
    private String name;

    @Column(name = "type")
    @Header
    private String type;

    @Column(name = "host")
    @Header
    private String host;

    @Column(name = "port")
    @Header
    private String port;

    @Column(name = "username")
    @Header
    private String username;

    @Column(name = "password")
    @Header
    private String password;

    @Column(name = "database")
    @Header
    private String database;


    @org.springframework.data.annotation.Transient
    @Header
//    @Column(name = "desc")\
    private String desc;

    @Version
    @Column(name = "version")
    @Header
    private Integer version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "create_id")
    private Long createId;

    @org.springframework.data.annotation.Transient
    @Transient
    private ConnectionFactory connectionFactory;

    @org.springframework.data.annotation.Transient
    @Transient
    private DatabaseClient databaseClient;
}

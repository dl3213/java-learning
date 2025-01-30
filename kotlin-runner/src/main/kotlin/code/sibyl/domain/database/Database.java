package code.sibyl.domain.database;

import code.sibyl.aop.Header;
import code.sibyl.common.DataBaseTypeEnum;
import com.alibaba.excel.annotation.ExcelProperty;
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
@Table("T_SYS_DATABASE")
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
    @ExcelProperty("id")
    private Long id;

    @Column(name = "name")
    @Header
    @ExcelProperty("name")
    private String name;

    @Column(name = "type")
    @Header
    @ExcelProperty("type")
    private String type;

    @Column(name = "host")
    @Header
    @ExcelProperty("host")
    private String host;

    @Column(name = "port")
    @Header
    @ExcelProperty("port")
    private String port;

    @Column(name = "username")
    @Header
    @ExcelProperty("username")
    private String username;

    @Column(name = "password")
    @Header
    @ExcelProperty("password")
    private String password;

    @Column(name = "database")
    @Header
    @ExcelProperty("database")
    private String database;


    @org.springframework.data.annotation.Transient
    @Header
//    @Column(name = "desc")\
    private String desc;

    @Version
    @Column(name = "version")
    @Header
    @ExcelProperty("version")
    private Integer version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    @ExcelProperty("createTime")
    private LocalDateTime createTime;

    @Column(name = "create_id")
    @ExcelProperty("createId")
    private Long createId;

    @org.springframework.data.annotation.Transient
    @Transient
    private ConnectionFactory connectionFactory;

    @org.springframework.data.annotation.Transient
    @Transient
    private DatabaseClient databaseClient;

    public static Database _default() {
        return new Database().setId(0L).setName("H2").setType(DataBaseTypeEnum.h2.getCode()).setDesc("default");
    }
}

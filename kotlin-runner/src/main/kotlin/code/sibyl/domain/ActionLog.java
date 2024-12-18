package code.sibyl.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "T_SYS_ACTION_LOG")
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @org.springframework.data.annotation.Id
    private Long id;

    private Long userId;

    private String systemKey;

    private String topic;

    private String type;

    @Column(name = "CLASS")
    private String clazz;

    private String method;

    private String url;

    private String ip;

    private String requestHeader;
    private String requestFormData;

    private Object requestJson;

    private Object responseJson;

    private Object bodyJson;

    private Object throwable;

    private String isDeleted = "0";

    private LocalDateTime createTime;

    private Long createId;

}
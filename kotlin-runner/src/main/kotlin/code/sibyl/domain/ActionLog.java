package code.sibyl.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "T_SYS_ACTION_LOG")
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @org.springframework.data.annotation.Id
    private Long id;

    @Column(name = "TOPIC")
    private String topic;

    @Column(name = "TYPE", length = 16)
    private String type;

    @Column(name = "CLASS")
    private String classField;

    @Column(name = "METHOD")
    private String method;

    @Column(name = "URL")
    private String url;

    @Column(name = "IP", length = 64)
    private String ip;

    @Column(name = "REQUEST_JSON", columnDefinition = "JSON(0, 0)")
    private Object requestJson;

    @Column(name = "RESPONSE_JSON", columnDefinition = "JSON(0, 0)")
    private Object responseJson;

    @Column(name = "BODY_JSON", columnDefinition = "JSON(0, 0)")
    private Object bodyJson;

    @Column(name = "THROWABLE", columnDefinition = "JSON(0, 0)")
    private Object throwable;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Column(name = "CREATE_ID")
    private Long createId;

}
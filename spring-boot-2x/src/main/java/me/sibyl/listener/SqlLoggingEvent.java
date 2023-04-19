package me.sibyl.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.ApplicationEvent;

/**
 * @Classname SqlLoggingEvent
 * @Description SqlLoggingEvent
 * @Date 2023/4/19 16:24
 * @Author by Qin Yazhi
 */
@Data
public class SqlLoggingEvent extends ApplicationEvent {

    private ProceedingJoinPoint pjp;
    private Thread eventThread;

    public SqlLoggingEvent(Object source, ProceedingJoinPoint pjp, Thread currentThread) {
        super(source);
        this.pjp = pjp;
        this.eventThread = currentThread;
    }
}

package code.sibyl.event;

import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SibylEvent extends ApplicationEvent {

    private String event;

    public SibylEvent(Object source, String event) {
        super(source);
        this.event = event;
    }


}

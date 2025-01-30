package code.sibyl.event;

import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class Event extends ApplicationEvent {

    private String event;

    public Event(Object source, String event) {
        super(source);
        this.event = event;
    }


}

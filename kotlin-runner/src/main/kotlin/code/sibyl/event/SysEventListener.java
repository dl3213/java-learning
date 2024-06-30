package code.sibyl.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SysEventListener {

    @Async
    @EventListener(classes = SibylEvent.class)
    public void doOnEvent(SibylEvent event) {
        log.info("[SibylEvent-{}]-------------------------------------", event.getEvent());
    }
}

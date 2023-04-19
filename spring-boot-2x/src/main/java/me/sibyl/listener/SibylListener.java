package me.sibyl.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Classname SibylListener
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/18 17:54
 */
@Component
@Slf4j
public class SibylListener {

    @EventListener
    public void listener(SibylEvent event) {
        log.info(String.format("%s监听到事件源：%s.", SibylListener.class.getName(), event.getSource()));

    }
}

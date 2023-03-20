package me.sibyl.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @Classname SibylEvent
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/18 17:54
 */

public class SibylEvent extends ApplicationEvent {

    public SibylEvent(Object source) {
        super(source);
    }
}

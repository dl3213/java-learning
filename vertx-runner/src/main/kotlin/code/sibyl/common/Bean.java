package code.sibyl.common;

import code.sibyl.service.BeanService;

public interface Bean {
    default void init() {
        BeanService.getInstance().put(this.getClass(), this);
    }
}

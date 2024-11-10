package code.sibyl.service;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class BeanService {

    public static void main(String[] args) {
        System.err.println(SystemService.getInstance());
        System.err.println(BeanService.getInstance().getMap());
        System.err.println(SystemService.getInstance() == BeanService.getInstance().getBean(SystemService.class));
    }

    private BeanService() {

    }

    private static BeanService instance = new BeanService();

    @Getter
    public Map<Class, Object> map = new HashMap();

    public static BeanService getInstance() {
        return instance;
    }

    public Object getBean(Class clazz) {
        return this.map.get(clazz);
    }

    public Object put(Class clazz, Object bean) {
        this.map.put(clazz, bean);
        return this;
    }
}

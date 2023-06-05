package me.sibyl.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @author dyingleaf3213
 * @Classname BeanContextUtil
 * @Description TODO
 * @Create 2023/06/05 20:46
 */
@Component
public class BeanContextUtil implements BeanFactoryAware {

    private  static BeanFactory beanFactory;

    public static Object getContextBean(String  beanName){
        return beanFactory.getBean(beanName);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        BeanContextUtil.beanFactory = beanFactory;
    }

    public static Object getBean(String beanName){
        return BeanContextUtil.getContextBean(beanName);
    }
}

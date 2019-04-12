package com.zsyc.warehouse.task.util;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 说明 不依赖servlet context获取Spring Application Contexts
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext contex) {
        SpringContextUtil.context = contex;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(String beanId, Class<T> c) {
        return (T) context.getBean(beanId, c);
    }

    /**
     * 获取对象
     * @param <T>
     * @return T
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return context.getBean(requiredType);
    }

}


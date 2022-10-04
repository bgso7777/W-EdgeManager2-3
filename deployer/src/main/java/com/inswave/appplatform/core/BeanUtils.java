package com.inswave.appplatform.core;

import org.springframework.context.ApplicationContext;

public class BeanUtils {
    public static Object getBean(String bean){
        ApplicationContext applicationContext = ApplicationContectProvider.getApplicationContext();
        return applicationContext.getBean(bean);
    }

    public static Object getBeanByClass(Class packageName){
        ApplicationContext applicationContext = ApplicationContectProvider.getApplicationContext();
        return applicationContext.getBean(packageName);
    }
}

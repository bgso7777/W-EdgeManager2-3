package com.inswave.appplatform.transaver.util;

import org.springframework.context.ApplicationContext;

public class BeanUtils {
    public static <T>T getBean(Class<T> type) {
        ApplicationContext applicationContext = ApplicationContextProvider.getContext();
        return applicationContext.getBean(type);
    }
}

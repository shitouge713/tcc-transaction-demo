package org.mengyun.tcctransaction.sample.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 普通类获取bean
 */
@Component
public class SpringUtil {
    @Resource
    private ApplicationContext applicationContext;

    public Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public <T> List<T> getSortedBeansOfType(Class<T> clazz) {
        //spring启动后初始化
        Map<String, T> beansOfType =  applicationContext.getBeansOfType(clazz);
        List<T> list = new ArrayList<>(beansOfType.values());
        //按照spring的规范（@Order等）排序
        AnnotationAwareOrderComparator.sort(list);
        return list;
    }
}
package com.wangxb.beans;

import com.wangxb.web.mvc.WxbController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    private static Map<Class<?>,Object> classMap = new ConcurrentHashMap<>();

    public static Object getBean(Class<?> cls) {
        return classMap.get(cls);
    }

    public static void initBean(List<Class<?>> classList) throws Exception {
        List<Class<?>> toCreate = new ArrayList<>(classList);
        while(toCreate.size() != 0){
            int remainSize = toCreate.size();
            for (int i = 0;i<toCreate.size();i++) {
                if(finishCreate(toCreate.get(i))){
                    toCreate.remove(i);
                }
            }
            if(toCreate.size() == remainSize){
                throw new Exception("cycle dependency");
            }
        }
    }

    public static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        if(!cls.isAnnotationPresent(WxbBean.class) && !cls.isAnnotationPresent(WxbController.class)){
            return true;
        }
        Object bean = cls.newInstance();
        for (Field field : cls.getDeclaredFields()) {
            if(field.isAnnotationPresent(WxbAutowired.class)){
                Class<?> fieldType = field.getType();
                Object reliantBean = BeanFactory.getBean(fieldType);
                if(null == reliantBean){
                    return false;
                }
                field.setAccessible(true);
                field.set(bean,reliantBean);
            }
        }
        classMap.put(cls,bean);
        return true;
    }
}

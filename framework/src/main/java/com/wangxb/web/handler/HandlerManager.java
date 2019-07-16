package com.wangxb.web.handler;

import com.wangxb.web.mvc.WxbController;
import com.wangxb.web.mvc.WxbRequestMapping;
import com.wangxb.web.mvc.WxbRequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    public static void resolveMappingHandler(List<Class<?>> classList){
        for (Class<?> cls : classList) {
            if(cls.isAnnotationPresent(WxbController.class)){
                parseHandlerFromController(cls);
            }
        }
    }

    private static  void parseHandlerFromController(Class<?> cls){
        String uri = "";
        if (cls.isAnnotationPresent(WxbRequestMapping.class)) {
            uri = cls.getAnnotation(WxbRequestMapping.class).value();
        }
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if(!method.isAnnotationPresent(WxbRequestMapping.class)){
                continue;
            }
            uri += method.getDeclaredAnnotation(WxbRequestMapping.class).value();
            List<String> paramNameList = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                if(parameter.isAnnotationPresent(WxbRequestParam.class)){
                    paramNameList.add(parameter.getDeclaredAnnotation(WxbRequestParam.class).value());
                }
            }
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);

            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);
            mappingHandlerList.add(mappingHandler);
        }

    }
}

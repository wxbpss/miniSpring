package com.wangxb.starter;

import com.wangxb.beans.BeanFactory;
import com.wangxb.core.ClassScanner;
import com.wangxb.web.handler.HandlerManager;
import com.wangxb.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.io.IOException;
import java.util.List;

public class MiniApplication {
    public static void run(Class<?> cls,String[] args){
        System.out.println("Helli mini-spring");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            BeanFactory.initBean(classList);
            HandlerManager.resolveMappingHandler(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

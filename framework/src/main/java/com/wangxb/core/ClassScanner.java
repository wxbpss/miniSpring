package com.wangxb.core;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        String path = packageName.replace(".","/");
        //获取类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(path);
        File classPathDir = new File(url.getFile());
        for (File file : classPathDir.listFiles()) {
            if (file.isDirectory()) {
                classList.addAll(scanClasses(packageName+"."+file.getName()));
            }else{
                String className = packageName + "." + file.getName().replace(".class","");
                classList.add(Class.forName(className));
            }
        }
        return classList;
    }

    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()){
            JarEntry jarEntry = entries.nextElement();
            String entryName = jarEntry.getName();
            if(entryName.startsWith(path) && entryName.endsWith(".class")){
                String classFullName = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                classList.add(Class.forName(classFullName));
            }
        }
        return classList;
    }

    private static List<Class<?>> getClassesFromFile(String absPath,String path,  File file) throws ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File file1 : files) {
            if(file1.isDirectory()){
                classList.addAll(getClassesFromFile(absPath,path,file1));
            }else if(file1.isFile()){
                String absolutePath = file1.getAbsolutePath();
                if (absolutePath.endsWith(".class")) {
                    String classFullName =path+"."+ absolutePath.replace(absPath,"").replace("/", ".").substring(0, absolutePath.length() - 6);
                    classList.add(Class.forName(classFullName.replace("..",".")));
                }
            }
        }
        return classList;
    }
}

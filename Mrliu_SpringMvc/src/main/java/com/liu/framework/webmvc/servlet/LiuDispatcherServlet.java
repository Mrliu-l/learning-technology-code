package com.liu.framework.webmvc.servlet;

import com.liu.framework.Tools.Tools;
import com.liu.framework.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jdk.nashorn.api.scripting.ScriptUtils.convert;

/**
 * @author liu_l
 * @Title: LiuDispatcherServlet
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/2610:42
 */
public class LiuDispatcherServlet extends HttpServlet {

    //配置文件信息
    private Properties contextConfig = new Properties();
    //加注解的类名集合
    private List<String> clazzName = new ArrayList<String>();
    //IOC
    private Map<String, Object> ioc = new HashMap<String, Object>();
    //repuestMapping
    private Map<String, Handle> handleMapping = new HashMap<String, Handle>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //等待请求
        doDispatch(req,resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp){
        try {
            Handle handle = getHandle(req);
            if(handle == null){
                resp.getWriter().write("404 Not Found!!!");
            }

            //获取方法参数列表
            Class<?>[] paramTypes = handle.method.getParameterTypes();
            //保存所有需要赋值的参数
            Object[] paramValues = new Object[paramTypes.length];

            Map<String, String[]> paramMap = req.getParameterMap();
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]","");
                if(!handle.paramIndexMapping.containsKey(entry.getKey())){continue;}
                //如果匹配上，则开始填充数据
                int index = handle.paramIndexMapping.get(entry.getKey());
                paramValues[index] =convert(value,paramTypes[index]);
            }

            //设置方法中的request和response对象
            int reqIndex = handle.paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
            int respIndex = handle.paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
            handle.method.invoke(handle.controller,paramValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  Handle getHandle(HttpServletRequest req){
        if(handleMapping.isEmpty()){return null;}

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath,"").replaceAll("/+","/");

        for (Map.Entry<String, Handle> entry : handleMapping.entrySet()) {
            Handle handle = entry.getValue();
            Matcher matcher = handle.pattern.matcher(url);
            //如果没匹配上则匹配下一个
            if(!matcher.matches()){continue;}
            return handle;
        }
        return  null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //spring启动

        //1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //2.扫描所有相关的类
        doScanner(contextConfig.getProperty("scanPackage"));
        //3.实例化所有扫描到的类
        doInstance();
        //4、自动注入
        doAutowire();
        //5、初始化HandleMapping
        initHandleMapping();
        //6.等待请求
    }

    private class Handle{
        protected Object controller;
        protected Method method;
        protected Pattern pattern;
        protected Map<String, Integer> paramIndexMapping;

        public Handle(Object controller, Method method, Pattern pattern) {
            this.controller = controller;
            this.method = method;
            this.pattern = pattern;
            this.paramIndexMapping = new HashMap<String, Integer>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method) {
            //提取方法中加入了注解的参数
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if(annotation instanceof MrLiuRequestParam){
                        String paramName = ((MrLiuRequestParam) annotation).value();
                        if(!"".equals(paramName.trim())){
                            paramIndexMapping.put(paramName,i);
                        }
                    }
                }       
            }

            //提取方法中的request和response
            Class<?>[] paramTypes = method.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                Class<?> type = paramTypes[i];
                if(type == HttpServletRequest.class || type == HttpServletResponse.class){
                    paramIndexMapping.put(type.getName(),i);
                }
            }
        }
    }

    private void initHandleMapping() {
        if(!ioc.isEmpty()){
            for (Map.Entry<String, Object> entry : ioc.entrySet()) {
                //拿到controller的url
                Class<?> clazz = entry.getValue().getClass();
                if(!clazz.isAnnotationPresent(MrLiuController.class)){continue;}

                String baseUrl = "";
                if(clazz.isAnnotationPresent(MrLiuRequestMapping.class)){
                    MrLiuRequestMapping mrLiuRequestMapping = clazz.getAnnotation(MrLiuRequestMapping.class);
                    baseUrl = mrLiuRequestMapping.value();
                }else{
                    baseUrl = Tools.lowerFirstCase(clazz.getName());
                }
                //拿到method的url
                for (Method method : clazz.getMethods()) {
                    if(!method.isAnnotationPresent(MrLiuRequestMapping.class)){continue;}
                    MrLiuRequestMapping methodMapping = method.getAnnotation(MrLiuRequestMapping.class);
                    String methodUrl = ("/" + baseUrl + methodMapping.value()).replaceAll("/+","/");
                    Pattern pattern = Pattern.compile(methodUrl);
                    handleMapping.put(methodUrl,new Handle(entry.getValue(),method,pattern));
                    System.out.println("Mapping : " + methodUrl + "," + pattern);
                }

            }
        }
    }

    private void doAutowire() {
        if(!ioc.isEmpty()){
            for (Map.Entry<String, Object> entry : ioc.entrySet()) {
                Field[] fields = entry.getValue().getClass().getDeclaredFields();
                for(Field field : fields){
                    if(!field.isAnnotationPresent(MrLiuAutowire.class)){continue;}

                    MrLiuAutowire autowire = field.getAnnotation(MrLiuAutowire.class);
                    String beanName = autowire.value();
                    if("".equals(beanName)){
                        beanName = field.getType().getName();
                    }
                    //强制访问
                    field.setAccessible(true);
                    try {
                        field.set(entry.getValue(),ioc.get(beanName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
    }

    private void doInstance() {
        if(clazzName.isEmpty()){return;}
        for (String className : clazzName) {
            try {
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(MrLiuController.class)){
                    //Controller默认采用首字母小写
                    String beanName = Tools.lowerFirstCase(clazz.getName());
                    ioc.put(beanName,clazz.newInstance());
                }else if(clazz.isAnnotationPresent(MrLiuService.class)){

                    //2、自定义了bean名称
                    MrLiuService mrLiuService = clazz.getAnnotation(MrLiuService.class);
                    String beanName = mrLiuService.value();
                    //1、默认采用首字母小写
                    if("".equals(beanName.trim())){
                        beanName = Tools.lowerFirstCase(clazz.getName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName,instance);
                    //3、根据结构名字来赋值
                    for (Class<?> aClass : clazz.getInterfaces()) {
                        //此处未处理一个接口多个实现类时应该抛异常的代码
                        ioc.put(aClass.getName(), instance);
                    }
                }else{
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }   
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {

            if(file.isDirectory()){
               doScanner(scanPackage + "." + file.getName());
            }else{
                String className = file.getName().replace(".class", "");
                clazzName.add(scanPackage + "." + className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

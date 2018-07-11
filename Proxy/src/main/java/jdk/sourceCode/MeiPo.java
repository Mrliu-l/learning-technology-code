package jdk.sourceCode;


import java.lang.reflect.Method;

/**
 * @author liu_l
 * @Title: MeiPo
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/7/512:14
 */
public class MeiPo implements MrliuInvocationHandler {

    private Object target;

    public Object getInstance(Person person){
        this.target = person;
        Class clazz = person.getClass();
        System.out.println("new的对象：" + clazz);
        return MrliuProxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我是媒婆：" + "得给你找个异性才行");
        System.out.println("开始进行海选...");
        System.out.println("------------");

        //调用的时候
        method.invoke(this.target, args);
        System.out.println("------------");
        System.out.println("如果合适的话，就准备办事");
        return null;
    }
}

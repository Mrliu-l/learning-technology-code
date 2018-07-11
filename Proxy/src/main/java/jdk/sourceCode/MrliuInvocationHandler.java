package jdk.sourceCode;

import java.lang.reflect.Method;

/**
 * @author liu_l
 * @Title: MrliuInvocationHandler
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/7/512:24
 */
public interface MrliuInvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable;
}

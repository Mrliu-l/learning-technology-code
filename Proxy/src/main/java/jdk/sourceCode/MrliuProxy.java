package jdk.sourceCode;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;

/**
 * @author liu_l
 * @Title: MrliuProxy
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/7/512:25
 */
public class MrliuProxy {

    protected MrliuInvocationHandler h;

    private MrliuProxy() {
    }

    protected MrliuProxy(MrliuInvocationHandler h) {
        this.h = h;
    }

    private static String ln = "\r\n";

    public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          MrliuInvocationHandler h) {
        try {
            //1、生成源代码
            String proxySrc = generateSrc(interfaces[0]);

            //2、生成.java文件
            String filePath = MrliuProxy.class.getResource("").getPath();
            System.out.println(filePath + "$Proxy0.java");
            File file = new File(filePath + "$Proxy0.java");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(proxySrc);
            fileWriter.flush();
            fileWriter.close();

            //3、编译.java文件，编译为.class文件
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = javaCompiler.getStandardFileManager(null,null,null);

            //4、加载到jvm中
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    private static String generateSrc(Class<?> oneInterface){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("import java.lang.reflect.*;" + ln);
        stringBuffer.append("import jdk.example.Person;" + ln);
        stringBuffer.append("public final class $Proxy0 extends MrliuProxy\n" +
                "\timplements " + oneInterface.getName() + "{" + ln);
        stringBuffer.append("public $Proxy0(MrliuInvocationHandler invocationhandler){" + ln);
        stringBuffer.append("super(invocationhandler);" + ln);
        stringBuffer.append("}" + ln);
        for(Method method : oneInterface.getMethods()){
            stringBuffer.append("public final " + method.getReturnType() + " " + method.getName() + "() throws Throwable{" + ln);
            stringBuffer.append("Method m = Class.forName(\"" + oneInterface.getName() + "\").getMethod(\"" + method.getName() + "\", new Class[0]);" + ln);
            stringBuffer.append("super.h.invoke(this, m, null);" + ln);
            stringBuffer.append("return;" + ln);
            stringBuffer.append("}" + ln);
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

package jdk.example;

import sun.misc.ProxyGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liu_l
 * @Title: TestFindLove
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/7/512:12
 */
public class TestFindLove {
    public static void main(String[] args) throws IOException {
        XingXing xingXing = new XingXing();
        Person proxyXing = (Person) new MeiPo().getInstance(xingXing);
        System.out.println("代理对象：" + proxyXing.getClass());
        proxyXing.findLove();

        //获取字节码内容
        byte[] data = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{Person.class});
        FileOutputStream os = new FileOutputStream("E:/$Proxy0.class");
        os.write(data);
        os.close();
    }
}

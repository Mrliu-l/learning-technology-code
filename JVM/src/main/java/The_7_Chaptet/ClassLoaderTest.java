package The_7_Chaptet;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu_l
 * @Title: ClassLoaderTest
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/3017:34
 */
public class ClassLoaderTest {

    @Test
    public void test() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object object = this.getClass().getClassLoader().loadClass("The_7_Chaptet.ClassLoaderTest").newInstance().getClass();
        System.out.println(object.getClass());
        System.out.println(this.getClass());
        System.out.println(object instanceof The_7_Chaptet.ClassLoaderTest);
    }
}

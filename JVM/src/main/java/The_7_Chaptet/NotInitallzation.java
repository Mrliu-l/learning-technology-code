package The_7_Chaptet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu_l
 * @Title: NotInitallzation
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/3016:48
 */
public class NotInitallzation {

    public static void main(String[] args) {
        System.out.println("*********验证子类调用父类的静态变量，不会初始化子类*********");
        String subtest = SubClass.test;
        System.out.println("*********验证通过数定义引用类，不会触发引用类初始化*********");
        List<SubClass> list = new ArrayList<SubClass>();
        System.out.println("*********验证调用类中定义的常量，不会触发引用类初始化*********");
        String supertest = SuperClass.test;
    }
}

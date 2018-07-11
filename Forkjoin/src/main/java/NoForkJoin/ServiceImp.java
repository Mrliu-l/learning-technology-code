package NoForkJoin;

import forkJoinTest.Row;
import forkJoinTest.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu_l
 * @Title: ServiceImp
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/240:32
 */
public class ServiceImp {
    
    public static void main(String[] args) throws InterruptedException {
        long s0 = System.currentTimeMillis();
        //造业务数据
        List<Student> list = new ArrayList<Student>();
        for (int i = 0; i < 10000; i++) {
            Student student = new Student();
            student.setName("test1" + i);
            student.setSax("man");
            student.setTall((double) i);
            list.add(student);
        }
        //开始业务数据处理
        List<Row> rows = new ArrayList<Row>();
        for (int i = 0; i < 10000; i++) {
            Student student = list.get(i);
            //模拟业务数据处理需耗时1毫秒
            Thread.sleep(1);
            Row row = new Row();
            row.put("name", student.getName());
            row.put("sax", student.getSax());
            row.put("tall", student.getTall());
            rows.add(row);
        }
        System.out.println("共处理业务对象：" + rows.size() + "个");
        System.out.println("共耗时：" + (System.currentTimeMillis() - s0) + "毫秒");
    }
}

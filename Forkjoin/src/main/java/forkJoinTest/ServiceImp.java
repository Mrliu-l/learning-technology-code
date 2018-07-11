package forkJoinTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * @author liu_l
 * @Title: ServiceImp
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/240:12
 */
public class ServiceImp {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long s0 = System.currentTimeMillis();
        //造业务数据
        List<Student> list = new ArrayList<Student>();
        for(int i = 0; i < 10000; i ++){
            Student student = new Student();
            student.setName("test1" + i);
            student.setSax("man");
            student.setTall((double)i);
            list.add(student);
        }
        //开始业务数据处理
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinCode studentForkJoinCode = ForkJoinCode.getInstance(0, list.size()-1, list);
        Future<List<Row>> result = pool.submit(studentForkJoinCode);
        System.out.println("共处理业务对象：" + result.get().size() + "个");
        showPoolStates(pool);
        System.out.println("共耗时：" + (System.currentTimeMillis() - s0) + "毫秒");
    }

    /**
     * @Description: 监控Fork/Join池相关方法
     * @param:
     * @author liu-lei
     * @date 2018/6/24 0:43
     */
    private  static  void showPoolStates(ForkJoinPool pool){
        System.out.println("*******************");
        System.out.println("线程池的worker线程数量：" + pool.getPoolSize());
        System.out.println("当前执行任务的线程数量：" + pool.getActiveThreadCount());
        System.out.println("没有被阻塞正在工作的线程：" + pool.getRunningThreadCount());
        System.out.println("已经提交给池还没有开始执行的任务数：" + pool.getQueuedSubmissionCount());
        System.out.println("已经提交给池开始执行的任务数：" + pool.getQueuedTaskCount());
        System.out.println("线程偷取任务数：" + pool.getStealCount());

    }
}

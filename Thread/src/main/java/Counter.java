import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liu_l
 * @Title: Counter
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/3010:10
 */
public class Counter {

    public static int count = 0;
    public static AtomicInteger atomicInteger = new AtomicInteger();

    public static void add(){
        count++;
        int i = atomicInteger.incrementAndGet();
        System.out.println("第" + count + "次访问:" + Thread.currentThread().getName());
    }
}

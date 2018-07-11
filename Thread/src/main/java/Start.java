import java.util.concurrent.CountDownLatch;

/**
 * @author liu_l
 * @Title: Start
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/3010:16
 */
public class Start {

    public static void main(String[] args) {
        final int threadNum = 10;
        final CountDownLatch cdl = new CountDownLatch(threadNum);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        cdl.await();
                        Counter.add();
                    }catch (Exception e){
                        e.getMessage();
                    }
                }
            }).start();
            cdl.countDown();
        }
    }

}

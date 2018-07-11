package forkJoinTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @author liu_l
 * @Title: ForkJoin
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/2323:09
 */
public class ForkJoinCode extends RecursiveTask<List<Row>>{

    protected static int THREAD_HOLD = 50;
    protected int start;//开始任务序号
    protected int end;//结束任务序号
    protected List<Student> datas;

    /**
     * @Description: TODO
     * @param:
     * @author liu-lei
     * @date 2018/6/23 23:19
     */
    public static ForkJoinCode getInstance(int start, int end, List<Student> datas){
        ForkJoinCode forkJoinCode = new ForkJoinCode();
        forkJoinCode.start = start;
        forkJoinCode.end = end;
        forkJoinCode.datas = datas;
        return forkJoinCode;
    }

    @Override
    protected List<Row> compute() {
        List<Row> rows = new ArrayList<Row>();
        boolean canCompute = (end - start) <= THREAD_HOLD;
        if(canCompute){
            for(int i = start; i <= end; i++){
                tranfromT2Row(rows, i);
            }
        }else{
            int middle = (start + end)/2;
            ForkJoinCode leftForkJoin = ForkJoinCode.getInstance(start, middle, datas);
            ForkJoinCode rightForkJoin = ForkJoinCode.getInstance(middle+1, end, datas);
            leftForkJoin.fork();
            rightForkJoin.fork();
            List<Row> lResult = leftForkJoin.join();
            List<Row> rResult = rightForkJoin.join();
            rows.addAll(lResult);
            rows.addAll(rResult);
        }
        return rows;
    }

    /**
     * @Description: 业务代码处理
     * @param:
     * @author liu-lei
     * @date 2018/6/24 0:33
     */
    public void tranfromT2Row(List<Row> rows, int i){
        Student student = datas.get(i);
        Row row = new Row();
        row.put("name", student.getName());
        row.put("sax", student.getSax());
        row.put("tall", student.getTall());
        try {
            //模拟业务数据处理需耗时1毫秒
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rows.add(row);
    };
}

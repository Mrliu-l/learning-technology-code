package forkJoinTest;

import java.io.Serializable;

/**
 * @author liu_l
 * @Title: Student
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/2323:03
 */
public class Student implements Serializable {

    private String name;

    private Double tall;

    private String sax;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTall() {
        return tall;
    }

    public void setTall(Double tall) {
        this.tall = tall;
    }

    public String getSax() {
        return sax;
    }

    public void setSax(String sax) {
        this.sax = sax;
    }
}

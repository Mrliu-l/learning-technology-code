package com.liu.demo.action;

import com.liu.demo.service.ITestService;
import com.liu.framework.annotation.MrLiuAutowire;
import com.liu.framework.annotation.MrLiuController;
import com.liu.framework.annotation.MrLiuRequestMapping;
import com.liu.framework.annotation.MrLiuRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liu_l
 * @Title: TestAction
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/2611:02
 */
@MrLiuController
@MrLiuRequestMapping("testAction")
public class TestAction {

    @MrLiuAutowire
    private ITestService testService;

    @MrLiuRequestMapping("/getName")
    public void getName(HttpServletRequest req, HttpServletResponse resp){
        String result = testService.getName();
        try {
            resp.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MrLiuRequestMapping("/addName")
    public void addName(HttpServletRequest req, HttpServletResponse resp,
                        @MrLiuRequestParam("name") String name){
        String result = testService.addName(name);
        try {
            resp.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

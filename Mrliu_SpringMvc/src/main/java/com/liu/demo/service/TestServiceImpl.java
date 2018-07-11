package com.liu.demo.service;

import com.liu.framework.annotation.MrLiuService;

/**
 * @author liu_l
 * @Title: TestServiceImpl
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/2611:02
 */
@MrLiuService
public class TestServiceImpl implements ITestService{
    @Override
    public String getName() {
        return "Mrliu";
    }

    @Override
    public String addName(String name) {
        return "添加名字：" + name + "成功";
    }
}

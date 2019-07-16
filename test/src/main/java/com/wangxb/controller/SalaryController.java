package com.wangxb.controller;

import com.wangxb.beans.WxbAutowired;
import com.wangxb.service.SalaryService;
import com.wangxb.web.mvc.WxbController;
import com.wangxb.web.mvc.WxbRequestMapping;
import com.wangxb.web.mvc.WxbRequestParam;

@WxbController
@WxbRequestMapping("/test")
public class SalaryController {
    @WxbAutowired
    public SalaryService salaryService;

    @WxbRequestMapping("/getSalary.json")
    public String getSalary(@WxbRequestParam("name") String name, @WxbRequestParam("experience") String experience) {
        Integer salary = salaryService.calSalary(Integer.parseInt(experience));
        return  name+"的工资："+salary;
    }
}

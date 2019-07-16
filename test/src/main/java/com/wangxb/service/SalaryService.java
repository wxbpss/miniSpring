package com.wangxb.service;

import com.wangxb.beans.WxbBean;

@WxbBean
public class SalaryService {
    public Integer calSalary(Integer experience) {
        return experience * 5000;
    }
}

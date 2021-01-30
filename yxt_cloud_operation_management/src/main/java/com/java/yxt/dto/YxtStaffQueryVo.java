package com.java.yxt.dto;

import java.util.List;

/**
 * 客户信息请求类
 * @author zanglei
 */
public class YxtStaffQueryVo {
   private List<Long> list;

    public List<Long> getList() {
        return list;
    }

    public void setList(List<Long> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "YxtStaffQueryVo{" +
                "list=" + list +
                '}';
    }
}

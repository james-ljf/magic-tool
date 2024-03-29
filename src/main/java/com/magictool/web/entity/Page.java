package com.magictool.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询封装结果集
 * @Author ljf
 * @Date 2021/11/10 11:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> implements Serializable {

    private static final long serialVersionUID = -1984331333988532795L;

    /** 总记录数 */
    private long totalCount = 0;

    /** 当前页 */
    private int pageNum = 1;

    /** 页面记录大小 */
    private int pageSize = 10;

    /** 对象列表 */
    private List<T> resultList = new ArrayList<>();

    public Page(long totalCount, List<T> resultList){
        this.totalCount = totalCount;
        this.resultList = resultList;
    }
}


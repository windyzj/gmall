package com.atguigu.gmall.bean;

import java.util.List;

/**
 * @param
 * @return
 */
public class PageResult<T> {
    int from;

    int to;

    int total;



    List<T> resultList;


}

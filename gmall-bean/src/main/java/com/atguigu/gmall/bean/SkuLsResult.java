package com.atguigu.gmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @param
 * @return
 */
public class SkuLsResult implements Serializable {

    List<SkuLsInfo> skuLsInfoList;

    long total;

    long totalPages;

    List<String> attrIdList;

    public List<SkuLsInfo> getSkuLsInfoList() {
        return skuLsInfoList;
    }

    public void setSkuLsInfoList(List<SkuLsInfo> skuLsInfoList) {
        this.skuLsInfoList = skuLsInfoList;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<String> getAttrIdList() {
        return attrIdList;
    }

    public void setAttrIdList(List<String> attrIdList) {
        this.attrIdList = attrIdList;
    }
}

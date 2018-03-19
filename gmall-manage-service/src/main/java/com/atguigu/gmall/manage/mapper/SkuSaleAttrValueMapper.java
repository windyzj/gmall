package com.atguigu.gmall.manage.mapper;


import com.atguigu.gmall.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @param
 * @return
 */
public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {
    public List<SkuSaleAttrValue> selectSkuSaleAttrValueListBySpu(long spuId);
}

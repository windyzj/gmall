package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @param
 * @return
 */
public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {

    public List<SpuSaleAttr> selectSpuSaleAttrList(long spuId);

    public List<SpuSaleAttr>  selectSpuSaleAttrListCheckBySku(long skuId,long spuId);
}

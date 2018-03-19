package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.SkuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @param
 * @return
 */
public interface SkuInfoMapper extends Mapper<SkuInfo> {

      public  List<SkuInfo>  selectSkuInfoListBySpu(long spuId);

      public  List<String> selectSkuIdListByCatalog3Id(long catalog3Id);

      public  List<String> selectSkuIdListByValueIds(String valueIds);
}

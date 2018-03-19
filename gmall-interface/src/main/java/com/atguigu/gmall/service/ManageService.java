package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;

import java.util.List;
import java.util.Map;

public interface ManageService {

    public List<BaseCatalog1> getCatalog1();

    public List<BaseCatalog2> getCatalog2(String catalog1Id);

    public List<BaseCatalog3> getCatalog3(String catalog2Id);

    public List<BaseAttrInfo> getAttrList(String catalog3Id);

    public List<BaseAttrInfo> getAttrList(List<String> attrIdList);

    public void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    public BaseAttrInfo getAttrInfo(String id);

    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    public List<BaseSaleAttr> getBaseSaleAttrList();

    public void saveSpuInfo(SpuInfo spuInfo);

    public List<SpuImage> getSpuImageList(String spuId);

    public  List<SpuSaleAttr> getSpuSaleAttrList(String spuId);

    public  List<SpuSaleAttrValue> getSpuSaleAttrValueList(SpuSaleAttrValue spuSaleAttrValue);

    public void saveSkuInfo(SkuInfo skuInfo);

    public List<SkuInfo> getSkuInfoListBySpu(String spuId);

    public SkuInfo getSkuInfo(String skuId);

    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(String skuId,String spuId);

    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);

}

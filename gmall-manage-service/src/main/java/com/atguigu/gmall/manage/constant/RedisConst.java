package com.atguigu.gmall.manage.constant;

/**
 * @param
 * @return
 */
public interface RedisConst {
    public final static String sku_prefix="sku:";

    public final static String skuInfo_suffix=":info";

    public final static String skuImage_field ="image";

    public final static String skuSaleAttr_field ="saleAttr";

    public final int   skuinfo_exp_sec=24*60*60;
}

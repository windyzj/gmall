package com.atguigu.gmall.manage.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.config.RedisUtil;
import com.atguigu.gmall.manage.constant.RedisConst;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.ListService;
import com.atguigu.gmall.service.ManageService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @return
 */
@com.alibaba.dubbo.config.annotation.Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    BaseCatalog1Mapper baseCatalog1Mapper;

    @Autowired
    BaseCatalog2Mapper baseCatalog2Mapper;

    @Autowired
    BaseCatalog3Mapper baseCatalog3Mapper;

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    SpuImageMapper spuImageMapper;

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;



    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;


    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    SkuImageMapper skuImageMapper;

    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;


    @Reference
    ListService listService;

    @Override
    public List<BaseCatalog1> getCatalog1() {
        List<BaseCatalog1> baseCatalog1List = baseCatalog1Mapper.selectAll();
        return baseCatalog1List;
    }

    @Override
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        BaseCatalog2 baseCatalog2=new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);

        List<BaseCatalog2> baseCatalog2List = baseCatalog2Mapper.select(baseCatalog2);
        return baseCatalog2List;
    }

    @Override
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
        BaseCatalog3 baseCatalog3=new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);

        List<BaseCatalog3> baseCatalog3List = baseCatalog3Mapper.select(baseCatalog3);
        return baseCatalog3List;
    }

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3_id) {

        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.selectAttrInfoList(Long.parseLong(catalog3_id));
        return baseAttrInfoList;

    }


    @Override
    public List<BaseAttrInfo> getAttrList(List<String> attrIdList) {
        String attrIds = StringUtils.join(attrIdList.toArray(), ",");
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.selectAttrInfoListByIds(attrIds);
        return baseAttrInfoList;

    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {

        //如果有主键就进行更新，如果没有就插入
        if(baseAttrInfo.getId()!=null&&baseAttrInfo.getId().length()>0){
            baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);
        }else{
            //防止主键被赋上一个空字符串
            if(baseAttrInfo.getId().length()==0){
                baseAttrInfo.setId(null);
            }
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }
          //把原属性值全部清空
          BaseAttrValue baseAttrValue4Del = new BaseAttrValue();
          baseAttrValue4Del.setAttrId(baseAttrInfo.getId());
          baseAttrValueMapper.delete(baseAttrValue4Del);

          //重新插入属性
          if(baseAttrInfo.getAttrValueList()!=null&&baseAttrInfo.getAttrValueList().size()>0) {
            for (BaseAttrValue attrValue : baseAttrInfo.getAttrValueList()) {
                //防止主键被赋上一个空字符串
                if(attrValue.getId()!=null&&attrValue.getId().length()==0){
                    attrValue.setId(null);
                }
                attrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insertSelective(attrValue);
            }
        }
    }

    @Override
    public BaseAttrInfo getAttrInfo(String id) {
        //查询属性基本信息
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(id);

        //查询属性对应的属性值
        BaseAttrValue  baseAttrValue4Query =new BaseAttrValue();
        baseAttrValue4Query.setAttrId(baseAttrInfo.getId());
        List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.select(baseAttrValue4Query);

        baseAttrInfo.setAttrValueList(baseAttrValueList);
        return baseAttrInfo;
    }


    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo){
        List<SpuInfo> spuInfoList = spuInfoMapper.select(spuInfo);
        return  spuInfoList;
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return  baseSaleAttrMapper.selectAll();
    }

    public void saveSpuInfo(SpuInfo spuInfo){
        if(spuInfo.getId()==null||spuInfo.getId().length()==0){
            spuInfo.setId(null);
            spuInfoMapper.insertSelective(spuInfo);
        }else{
            spuInfoMapper.updateByPrimaryKey(spuInfo);
        }

        Example spuImageExample=new Example(SpuImage.class);
        spuImageExample.createCriteria().andEqualTo("spuId",spuInfo.getId());
        spuImageMapper.deleteByExample(spuImageExample);

        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if(spuImageList!=null) {
            for (SpuImage spuImage : spuImageList) {
                if(spuImage.getId()!=null&&spuImage.getId().length()==0){
                    spuImage.setId(null);
                }
                spuImage.setSpuId(spuInfo.getId());
                spuImageMapper.insertSelective(spuImage);
            }
        }

        Example spuSaleAttrExample=new Example(SpuSaleAttr.class);
        spuSaleAttrExample.createCriteria().andEqualTo("spuId",spuInfo.getId());
        spuSaleAttrMapper.deleteByExample(spuSaleAttrExample);


        Example spuSaleAttrValueExample=new Example(SpuSaleAttrValue.class);
        spuSaleAttrValueExample.createCriteria().andEqualTo("spuId",spuInfo.getId());
        spuSaleAttrValueMapper.deleteByExample(spuSaleAttrValueExample);

        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if(spuSaleAttrList!=null) {
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                if(spuSaleAttr.getId()!=null&&spuSaleAttr.getId().length()==0){
                    spuSaleAttr.setId(null);
                }
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insertSelective(spuSaleAttr);
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                    if(spuSaleAttrValue.getId()!=null&&spuSaleAttrValue.getId().length()==0){
                        spuSaleAttrValue.setId(null);
                    }
                    spuSaleAttrValue.setSpuId(spuInfo.getId());
                    spuSaleAttrValueMapper.insertSelective(spuSaleAttrValue);
                }
            }

        }
    }


    @Override
    public List<SpuImage> getSpuImageList(String spuId){
        SpuImage spuImage=new SpuImage();
        spuImage.setSpuId(spuId);
        List<SpuImage> spuImageList = spuImageMapper.select(spuImage);
        return spuImageList;
    }


    @Override
    public  List<SpuSaleAttr> getSpuSaleAttrList(String spuId){

        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.selectSpuSaleAttrList(Long.parseLong(spuId));
        return spuSaleAttrList;

    }

    @Override
    public  List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(String skuId,String spuId){

        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(Long.parseLong(skuId),Long.parseLong(spuId));
        return spuSaleAttrList;

    }



    @Override
    public List<SpuSaleAttrValue> getSpuSaleAttrValueList(SpuSaleAttrValue spuSaleAttrValue) {
        List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttrValueMapper.select(spuSaleAttrValue);

        return spuSaleAttrValueList;
    }


    public void saveSkuInfo(SkuInfo skuInfo){
            if(skuInfo.getId()==null||skuInfo.getId().length()==0){
                skuInfo.setId(null);
                skuInfoMapper.insertSelective(skuInfo);
            }else {
                skuInfoMapper.updateByPrimaryKeySelective(skuInfo);
            }


           Example example=new Example(SkuImage.class);
           example.createCriteria().andEqualTo("skuId",skuInfo.getId());
           skuImageMapper.deleteByExample(example);

            List<SkuImage> skuImageList = skuInfo.getSkuImageList();
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfo.getId());
                if(skuImage.getId()!=null&&skuImage.getId().length()==0) {
                    skuImage.setId(null);
                }
                skuImageMapper.insertSelective(skuImage);
            }


              Example skuAttrValueExample=new Example(SkuAttrValue.class);
              skuAttrValueExample.createCriteria().andEqualTo("skuId",skuInfo.getId());
              skuAttrValueMapper.deleteByExample(skuAttrValueExample);

             List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                if(skuAttrValue.getId()!=null&&skuAttrValue.getId().length()==0) {
                    skuAttrValue.setId(null);
                }
                skuAttrValueMapper.insertSelective(skuAttrValue);
            }


        Example skuSaleAttrValueExample=new Example(SkuSaleAttrValue.class);
        skuSaleAttrValueExample.createCriteria().andEqualTo("skuId",skuInfo.getId());
        skuSaleAttrValueMapper.deleteByExample(skuSaleAttrValueExample);

            List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValue.setId(null);
                skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);
            }


            //发送给列表模板
           sendSkuToList(  skuInfo);

    }



    public SpuInfo getSpuInfo(String spuId){
        SpuInfo spuInfo = spuInfoMapper.selectByPrimaryKey(spuId);

        SpuImage spuImage =new SpuImage();
        spuImage.setSpuId(spuId);
        List<SpuImage> spuImageList = spuImageMapper.select(spuImage);
        spuInfo.setSpuImageList(spuImageList);

        SpuSaleAttr spuSaleAttr =new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuId);
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.select(spuSaleAttr);

        spuInfo.setSpuSaleAttrList(spuSaleAttrList);

        return spuInfo;
    }


    public List<SkuInfo> getSkuInfoListBySpu(String spuId){
        List<SkuInfo> skuInfoList = skuInfoMapper.selectSkuInfoListBySpu(Long.parseLong(spuId));
        return  skuInfoList;

    }

    public SkuInfo getSkuInfo1(String skuId){

            Jedis jedis = redisUtil.getJedis();
            String skuKey= RedisConst.sku_prefix+skuId+RedisConst.skuInfo_suffix;
            String skuInfoJson = jedis.get(skuKey);
            if(skuInfoJson!=null ){
                System.err.println( Thread.currentThread().getName()+"：命中缓存"  );
                SkuInfo skuInfo = JSON.parseObject(skuInfoJson, SkuInfo.class);
                jedis.close();
                return skuInfo;
            }else{
                   System.err.println( Thread.currentThread().getName()+"：未命中缓存"  );

                    System.err.println( Thread.currentThread().getName()+"： 查询数据##################### ##" );
                    SkuInfo skuInfoDB = getSkuInfoDB(skuId);
                    String skuInfoJsonStr = JSON.toJSONString(skuInfoDB);
                    jedis.setex(skuKey,RedisConst.skuinfo_exp_sec,skuInfoJsonStr);
                    System.err.println( Thread.currentThread().getName()+"：数据库更新完毕############### #####" );
                    jedis.close();
                    return skuInfoDB;
            }
    }


    public SkuInfo getSkuInfo(String skuId){
        try {
            Jedis jedis = redisUtil.getJedis();
            String skuKey= RedisConst.sku_prefix+skuId+RedisConst.skuInfo_suffix;
            String skuInfoJson = jedis.get(skuKey);
            if(skuInfoJson!=null ){
                System.err.println( Thread.currentThread().getName()+"：命中缓存"  );
                if(skuInfoJson.equals("empty")){
                    return null;
                }
                SkuInfo skuInfo = JSON.parseObject(skuInfoJson, SkuInfo.class);
                return skuInfo;
            }else{
                System.err.println( Thread.currentThread().getName()+"：未命中缓存"  );
                String lock = jedis.set(RedisConst.sku_prefix + skuId + ":lock", "1", "NX", "PX", 10000);
                if("OK".equals(lock)){
                    System.err.println( Thread.currentThread().getName()+"：获得锁查询数据#####################"  );
                    SkuInfo skuInfoDB = getSkuInfoDB(skuId);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(skuInfoDB==null){
                        jedis.setex(skuKey,RedisConst.skuinfo_exp_sec,"empty");
                        jedis.del(RedisConst.sku_prefix + skuId + ":lock");
                        System.err.println( Thread.currentThread().getName()+"：数据库更新empty完毕########################"  );
                        jedis.close();
                        return skuInfoDB;
                    }

                    String skuInfoJsonStr = JSON.toJSONString(skuInfoDB);
                    jedis.setex(skuKey,RedisConst.skuinfo_exp_sec,skuInfoJsonStr);
                    jedis.del(RedisConst.sku_prefix + skuId + ":lock");

                    System.err.println( Thread.currentThread().getName()+"：数据库更新完毕########################"  );
                    jedis.close();
                    return skuInfoDB;
                }else{
                    System.err.println( Thread.currentThread().getName()+"：开始自旋"  );
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getSkuInfo(skuId);
                }

            }

        }catch (JedisConnectionException e){
            e.printStackTrace();
        }
        return  getSkuInfoDB(skuId);
    }


    public SkuInfo getSkuInfoDB(String skuId){

        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        if(skuInfo==null){
            return null;
        }
        SkuImage skuImage=new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImageList);

        SkuSaleAttrValue skuSaleAttrValue=new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuId);
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.select(skuSaleAttrValue);
        skuInfo.setSkuSaleAttrValueList(skuSaleAttrValueList);

        return  skuInfo;
    }

    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId){
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(Long.parseLong(spuId));
        return skuSaleAttrValueList;
    }



    private void sendSkuToList(SkuInfo skuInfo){

        SkuLsInfo skuLsInfo=new SkuLsInfo();

        try {
            BeanUtils.copyProperties(skuLsInfo, skuInfo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        listService.saveSkuInfo(skuLsInfo);

    }


}

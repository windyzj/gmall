package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @return
 */
@Controller
public class ItemController {


    @Reference
    ManageService manageService;

    @RequestMapping("/{skuId}.html")
    public String getSkuInfo(@PathVariable("skuId") String skuId, Model model){

        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        model.addAttribute("skuInfo",skuInfo);

        // 取出该spu的所有属性和属性值
/*        List<SpuSaleAttr> spuSaleAttrList = manageService.getSpuSaleAttrList(skuInfo.getSpuId());


        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();*/
        //比对两个列表如果有值 把spuSaleAttrValue的isCheck设为1



        // 优化的方式： 用一条select进行关联匹配   好处，利用mysql索引性能更好 ，可以把结果完整缓存。
        List<SpuSaleAttr> spuSaleAttrListCheckBySku = manageService.getSpuSaleAttrListCheckBySku(skuInfo.getId(), skuInfo.getSpuId());
        model.addAttribute("spuSaleAttrListCheckBySku",spuSaleAttrListCheckBySku);

        //获得有哪些
        List<SkuSaleAttrValue> skuSaleAttrValueListBySpu = manageService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());

        //把列表变换成 valueid1|valueid2|valueid3 ：skuId  的 哈希表 用于在页面中定位查询
        String valueIdsKey="";

        Map<String,String> valuesSkuMap=new HashMap<>();

        for (int i = 0; i < skuSaleAttrValueListBySpu.size(); i++) {
            SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueListBySpu.get(i);
            if(valueIdsKey.length()!=0){
                valueIdsKey= valueIdsKey+"|";
            }
            valueIdsKey=valueIdsKey+skuSaleAttrValue.getSaleAttrValueId();

            if((i+1)== skuSaleAttrValueListBySpu.size()||!skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueListBySpu.get(i+1).getSkuId())  ){

                valuesSkuMap.put(valueIdsKey,skuSaleAttrValue.getSkuId());
                valueIdsKey="";
            }

        }

        //把map变成json串
        String valuesSkuJson = JSON.toJSONString(valuesSkuMap);

        model.addAttribute("valuesSkuJson",valuesSkuJson);


        return "item";

    }


    @RequestMapping("/{skuId}.text")
    public String getSkuInfoText(@PathVariable("skuId") String skuId, Model model, HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().setAttribute("skuName","li4");
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        model.addAttribute("skuInfo",skuInfo);
        return "itemText";

    }



}

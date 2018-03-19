package com.atguigu.gmall.list.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.ListService;
import com.atguigu.gmall.service.ManageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import javassist.compiler.ast.Keyword;
import jdk.nashorn.internal.ir.IfNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.RequestWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @param
 * @return
 */
@Controller
public class ListController {

    @Reference
    ManageService manageService;

    @Reference
    ListService listService;


    @RequestMapping("list.html")
    public String getList(  SkuLsParams skuLsParams, Model model){


        List<BaseAttrInfo> attrList=null;

        SkuLsResult skuLsResult = listService.search(skuLsParams);

        if(skuLsParams.getCatalog3Id()!=null){
             attrList = manageService.getAttrList(skuLsParams.getCatalog3Id());
        }else{
            List<String> attrIdList = skuLsResult.getAttrIdList();
            attrList = manageService.getAttrList(attrIdList);
        }


        List<BaseAttrValueExt> selectedValuelist =new ArrayList<>();


        String[] valueIds=skuLsParams.getValueId();
        String catalog3Id = skuLsParams.getCatalog3Id();
        long totalPages = skuLsResult.getTotalPages();
        String keyword=skuLsParams.getKeyword();

        //去掉已选择的属性值
        if(skuLsParams.getValueId()!=null) {
            for (int i = 0; valueIds != null && i < valueIds.length; i++) {
                String valueId = valueIds[i];

                for (Iterator<BaseAttrInfo> iterator = attrList.iterator(); iterator.hasNext(); ) {
                    BaseAttrInfo baseAttrInfo = iterator.next();
                    List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
                    for (BaseAttrValue baseAttrValue : attrValueList) {
                        if (valueId.equals(baseAttrValue.getId())) {
                            BaseAttrValueExt baseAttrValueExt = new BaseAttrValueExt();
                            baseAttrValueExt.setAttrName(baseAttrInfo.getAttrName());
                            baseAttrValueExt.setId(baseAttrValue.getId());
                            baseAttrValueExt.setValueName(baseAttrValue.getValueName());
                            baseAttrValueExt.setAttrId(baseAttrInfo.getId());
                            String cancelUrlParam = makeUrlParam(keyword, valueIds, valueId);
                            baseAttrValueExt.setCancelUrlParam(cancelUrlParam);

                            selectedValuelist.add(baseAttrValueExt);

                            iterator.remove();// 如果选中了该属性 就从属性列表中去掉
                        }
                    }
                }
            }
        }
        String urlParam = makeUrlParam(keyword, valueIds, "");

        model.addAttribute("skuLsInfoList",skuLsResult.getSkuLsInfoList());

        model.addAttribute("selectedValueList", selectedValuelist);
        model.addAttribute("keyword",keyword);
        model.addAttribute("attrList",attrList);
        model.addAttribute("urlParam",urlParam);
        model.addAttribute("totalPages",totalPages);

        //以下是查询商品信息


        return "list";
    }


    private String  makeUrlParam(String keyword,   String[] valueIds,String excludeValueId ){
        String url="";
        List<String> paramList=new ArrayList<>();
        if(keyword!=null&&keyword.length()>0){
            paramList.add("keyword="+keyword);
        }

        if(valueIds!=null) {
            for (int i = 0; i < valueIds.length; i++) {
                String valueId = valueIds[i];
                if (!excludeValueId.equals(valueId)) {
                    paramList.add("valueId=" + valueId) ;
                }
            }
        }
         url = StringUtils.join(paramList, "&");
        return url;
    }
}

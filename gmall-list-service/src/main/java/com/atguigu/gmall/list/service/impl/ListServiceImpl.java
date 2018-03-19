package com.atguigu.gmall.list.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SkuLsInfo;
import com.atguigu.gmall.bean.SkuLsParams;
import com.atguigu.gmall.bean.SkuLsResult;
import com.atguigu.gmall.service.ListService;
import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.lucene.queryparser.xml.builders.BooleanFilterBuilder;

import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @param
 * @return
 */
@Service
public class ListServiceImpl implements ListService{

    @Autowired
    JestClient jestClient;


    //提供保存
    public void saveSkuInfo(SkuLsInfo skuLsInfo){
        Index index= new Index.Builder(skuLsInfo).index("gmall").type("SkuInfo").id(skuLsInfo.getId()).build();
        try {
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //删除

    public void delSkuInfo(SkuLsInfo skuLsInfo){
        Delete delete= new Delete.Builder(skuLsInfo.getId()).index("gmall").type("SkuInfo").build();
        try {
            jestClient.execute(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //查找 按关键词查找
     public SkuLsResult search(SkuLsParams skuLsParams){

         String query=makeQueryStringForSearch(skuLsParams);

         Search search= new Search.Builder(query).addIndex("gmall").addType("SkuInfo").build();
         SearchResult searchResult=null;
      try {
            searchResult = jestClient.execute(search);
         } catch (IOException e) {
            e.printStackTrace();
         }

         SkuLsResult skuLsResult = makeResultForSearch(skuLsParams, searchResult);

         return skuLsResult;

     }



    private String makeQueryStringForSearch(SkuLsParams skuLsParams){
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(skuLsParams.getKeyword()!=null){
            MatchQueryBuilder matchQueryBuilder=new MatchQueryBuilder("skuName",skuLsParams.getKeyword());       boolQueryBuilder.must(matchQueryBuilder);

            HighlightBuilder highlightBuilder=new HighlightBuilder();
            highlightBuilder.field("skuName");
            highlightBuilder.preTags("<span style='color:red'>");
            highlightBuilder.postTags("</span>");
            searchSourceBuilder.highlight(highlightBuilder);

            TermsBuilder groupby_attr = AggregationBuilders.terms("groupby_attr").field("skuAttrValueList.attrId");
            searchSourceBuilder.aggregation(groupby_attr);
        }
        if(skuLsParams.getCatalog3Id()!=null){
            QueryBuilder termQueryBuilder=new TermQueryBuilder("catalog3Id",skuLsParams.getCatalog3Id());
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if(skuLsParams.getValueId()!=null&&skuLsParams.getValueId().length>=0){
            QueryBuilder termsQueryBuilder=new TermsQueryBuilder("skuAttrValueList.valueId",skuLsParams.getValueId());
            boolQueryBuilder.filter(termsQueryBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder);

        int from =(skuLsParams.getPageNo()-1)*skuLsParams.getPageSize();
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(skuLsParams.getPageSize());

        searchSourceBuilder.sort("hotScore",SortOrder.DESC);

        String query = searchSourceBuilder.toString();

        System.err.println("query = " + query);
        return query;
    }

    private SkuLsResult makeResultForSearch(SkuLsParams skuLsParams,SearchResult searchResult){
        SkuLsResult skuLsResult=new SkuLsResult();
        List<SkuLsInfo> skuLsInfoList=new ArrayList<>(skuLsParams.getPageSize());

        List<SearchResult.Hit<SkuLsInfo, Void>> hits = searchResult.getHits(SkuLsInfo.class);
        for (SearchResult.Hit<SkuLsInfo, Void> hit : hits) {
            SkuLsInfo skuLsInfo = hit.source;
            if(hit.highlight!=null&&hit.highlight.size()>0){
                List<String> list = hit.highlight.get("skuName");
                String skuNameHl = list.get(0);
                skuLsInfo.setSkuName(skuNameHl);
            }
            skuLsInfoList.add(skuLsInfo);
        }
        skuLsResult.setSkuLsInfoList(skuLsInfoList);
        skuLsResult.setTotal(searchResult.getTotal());

        long totalPage= (searchResult.getTotal() + skuLsParams.getPageSize() -1) / skuLsParams.getPageSize();
        skuLsResult.setTotalPages(  totalPage);


        List<String> attrIdList=new ArrayList<>();
        MetricAggregation aggregations = searchResult.getAggregations();
        TermsAggregation groupby_attr = aggregations.getTermsAggregation("groupby_attr");
        if(groupby_attr!=null){
            List<TermsAggregation.Entry> buckets = groupby_attr.getBuckets();
            for (TermsAggregation.Entry bucket : buckets) {
                attrIdList.add( bucket.getKey()) ;
            }
            skuLsResult.setAttrIdList(attrIdList);
        }


        return skuLsResult;
    }

}

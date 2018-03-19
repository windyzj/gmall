package com.atguigu.gmall.list;

import com.atguigu.gmall.bean.SkuLsParams;
import com.atguigu.gmall.bean.SkuLsResult;
import com.atguigu.gmall.service.ListService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallListServiceApplicationTests {

	@Autowired
	JestClient jestClient;

	@Autowired
	ListService listService;

	@Test
	public void testEs() throws IOException {
		String query="{\n" +
				"  \"query\": {\n" +
				"    \"match\": {\n" +
				"      \"actorList.name\": \"张译\"\n" +
				"    }\n" +
				"  }\n" +
				"}";
		Search search = new Search.Builder(query).addIndex("movie_chn").addType("movie").build();


		SearchResult result = jestClient.execute(search);

		List<SearchResult.Hit<HashMap, Void>> hits = result.getHits(HashMap.class);

		for (SearchResult.Hit<HashMap, Void> hit : hits) {
			HashMap source = hit.source;
			System.err.println("source = " + source);



		}

	}

	@Test
	public void testSearch(){
		SkuLsParams skuLsParams=new SkuLsParams();
		skuLsParams.setKeyword("小米");
		skuLsParams.setCatalog3Id("61");
		skuLsParams.setPageSize(4);
		skuLsParams.setPageNo(2);
		skuLsParams.setValueId(new String[]{"45","46"});
		SkuLsResult skuLsResult = listService.search(skuLsParams);
		System.out.println("skuLsResult = " + skuLsResult);
	}


}

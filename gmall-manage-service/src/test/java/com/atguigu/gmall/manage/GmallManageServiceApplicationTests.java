package com.atguigu.gmall.manage;

import com.atguigu.gmall.manage.crawler.CatalogCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageServiceApplicationTests {

	@Autowired
	CatalogCrawler catalogCrawler;

	@Test
	public void doCrawl() {
		catalogCrawler.doCrawl();
	}

}

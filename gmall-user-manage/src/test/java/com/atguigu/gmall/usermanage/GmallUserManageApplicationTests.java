package com.atguigu.gmall.usermanage;


import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.service.UserManageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallUserManageApplicationTests {

	@Autowired
	UserManageService userManageService;

	@Test
	public void showAddressList() {
		List<UserAddress> userAddressList = userManageService.getUserAddressList("1");
		for (UserAddress userAddress : userAddressList) {
			System.err.println("userAddress = " + userAddress);
		}

	}










}

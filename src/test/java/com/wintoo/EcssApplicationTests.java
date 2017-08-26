package com.wintoo;

import com.wintoo.service.LoginService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EcssApplicationTests {

	@Autowired
	private LoginService loginService;

	@Test
	public void contextLoads() {
		loginService.addLog("12344","123","192.108.0.1");
	}

}

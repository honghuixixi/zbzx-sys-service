package com.aek.ebey.sys.web.controller;


import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.aek.common.core.Result;
import com.aek.ebey.sys.model.SysTenant;
import com.google.gson.Gson;

/**
 * 机构测试类
 * 
 * @author cyl
 * 
 * @since 2017年8月4日
 */
public class SysTenantControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysTenantControllerTest.class);
	
	private static final String TENANT_REQUEST_URI = "http://localhost:8090/sys/tenant";
	
	private static final String AEK56_TOKEN_KEY = "X-AEK56-Token";
	private static final String AEK56_TOKEN_VALUE = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNTI2Nzk0NzI2NCIsImF1ZCI6IndlYiIsImlzcyI6ImFlazU2LmNvbSIsImV4cCI6MTUwMTg1MDExNCwiZGV2aWNlSWQiOiIwZWM5NmZhYi1hODZlLWQ5NWQtNzQwMy04OWY4MDhhMmIxMmMifQ.uDnOPg8IMPx4F4n887h6twjM_z9-G-D32s4ZxKZ1dJgXl2wK-HTAQ5qY5l2J8JWl7Zt-kf4RCi1TlJfzt8Trtw";
	
	public HttpHeaders getHeaders(){
		HttpHeaders headers = new HttpHeaders();
		headers.add(AEK56_TOKEN_KEY, AEK56_TOKEN_VALUE);
		return headers;
	};
	
	
	@SuppressWarnings({ "unused", "rawtypes" })
	@Test
	public void testRecoverTenant() {
		
		
		/*String url = TENANT_REQUEST_URI + "/recover/2411";
		LOGGER.info("单元测试接口："+url);
		RestTemplate restTemplate = new RestTemplate();
		DateTime currDate = new DateTime();
		SysTenant tenant = new SysTenant();
		tenant.setEnable(true);
		tenant.setExpireTime(new Date());
		//tenant.setUpdateTime(currDate);
		String json = new Gson().toJson(tenant);
		LOGGER.info(json);
		HttpEntity<String> httpEntity = new HttpEntity<String>(json,this.getHeaders());
		
		ResponseEntity<Result> responseEntity = restTemplate.exchange(url, HttpMethod.POST,httpEntity,Result.class);
		Result result = responseEntity.getBody();
		
		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));*/
	}

}

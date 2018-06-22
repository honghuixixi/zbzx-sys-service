//package com.aek.ebey.base.web;
//
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//import com.aek.common.core.Result;
//import com.aek.ebey.base.enums.FieldTypeEnum;
//import com.aek.ebey.base.web.request.FieldRequest;
//import com.google.gson.Gson;
//
///**
// * 字段接口测试类
// *	
// * @author HongHui
// * @date   2017年7月14日
// */
//@SuppressWarnings("rawtypes")
//public class BaseFieldControllerTest {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(BaseFieldCategoryControllerTest.class);
//	
//	private static final String BASE_FIELD_REQUEST_URI = "http://localhost:8082/base/field";
//	
//	private static final String AEK56_TOKEN_KEY = "X-AEK56-Token";
//	private static final String AEK56_TOKEN_VALUE = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzU2NjY2NjY2NiIsImF1ZCI6Im1vYmlsZSIsImlzcyI6ImFlazU2LmNvbSIsImV4cCI6MTUwMDI4MzgzMiwiZGV2aWNlSWQiOiIwOGFlYjZjZi05NjQ0LTBhNmMtMDZjMS0yMjRiMGIzZWNiNmUifQ.yBz-Cm25MkQzL1NceNaZA7uDuyVgo4eaysm3joYc8-6C10Uln8ZKszR8ArWt6EFzHF6YypOn0BU3rA4zvsNhpQ";
//	
//	public HttpHeaders getHeaders(){
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(AEK56_TOKEN_KEY, AEK56_TOKEN_VALUE);
//		return headers;
//	}
//	
//	@Test
//	public void getBaseFieldPageTest(){
//		LOGGER.info("单元测试接口：/base/field/list");
//		RestTemplate restTemplate  = new RestTemplate();
//		String url = BASE_FIELD_REQUEST_URI + "/list?fieldCategoryId=1&pageNo=1&pageSize=9"; 
//		HttpEntity requestEntity = new HttpEntity<String>(this.getHeaders());
//		ResponseEntity<Result> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Result.class);
//		Result result = responseEntity.getBody();
//		//Result result = restTemplate.getForObject(url, Result.class);
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));
//	} 
//	
//	@Test
//	public void saveTest(){
//		//RestTemplate.postForObject()不带headers信息
//		/*LOGGER.info("单元测试接口：/base/field/save");
//		RestTemplate restTemplate  = new RestTemplate();
//		FieldRequest fieldRequest = new FieldRequest();
//		fieldRequest.setChineseName("生产商");
//		fieldRequest.setEnglishName("producer");
//		fieldRequest.setFieldType(FieldTypeEnum.TINYTEXT.getNumber());
//		fieldRequest.setFieldCategoryId(1l);
//		Result result = restTemplate.postForObject(BASE_FIELD_REQUEST_URI + "/save", fieldRequest,Result.class, "");
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));*/
//		
//		//RestTemplate.exchange()带headers信息
//		LOGGER.info("单元测试接口：/base/field/save");
//		RestTemplate restTemplate  = new RestTemplate();
//		//1.请求地址
//		String url = BASE_FIELD_REQUEST_URI + "/save";
//		//2.请求实体
//		FieldRequest fieldRequest = new FieldRequest();
//		fieldRequest.setChineseName("生产商");
//		fieldRequest.setEnglishName("producer");
//		fieldRequest.setFieldType(FieldTypeEnum.TINYTEXT.getNumber());
//		fieldRequest.setFieldCategoryId(1l);
//		//3.组装headers与请求实体类为HttpEntity
//		HttpEntity requestEntity = new HttpEntity<FieldRequest>(fieldRequest,this.getHeaders());
//		//4.发送POST请求
//		ResponseEntity<Result> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Result.class);
//		//5.响应实体
//		Result result = responseEntity.getBody();
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));
//	}
//	
//	@Test
//	public void getFieldByIdTest(){
//		/*LOGGER.info("单元测试接口：/base/field/search");
//		RestTemplate restTemplate  = new RestTemplate();
//		Result result = restTemplate.getForObject(BASE_FIELD_REQUEST_URI + "/search/1", Result.class);
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));*/
//		
//		LOGGER.info("单元测试接口：/base/field/search");
//		RestTemplate restTemplate  = new RestTemplate();
//		String url = BASE_FIELD_REQUEST_URI + "/search/1";
//		HttpEntity requestEntity = new HttpEntity<String>(this.getHeaders());
//		ResponseEntity<Result> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Result.class);
//		Result result = responseEntity.getBody();
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));
//	}
//	
//	@Test
//	public void editTest(){
//		LOGGER.info("单元测试接口：/base/field/edit");
//		RestTemplate restTemplate  = new RestTemplate();
//		FieldRequest fieldRequest = new FieldRequest();
//		fieldRequest.setChineseName("生产商");
//		fieldRequest.setId(2l);
//		Result result = restTemplate.postForObject(BASE_FIELD_REQUEST_URI + "/edit", fieldRequest,Result.class, "");
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));
//	}
//	
//	@Test
//	public void deleteTest(){
//		LOGGER.info("单元测试接口：/base/field/delete");
//		RestTemplate restTemplate  = new RestTemplate();
//		Result result = restTemplate.postForObject(BASE_FIELD_REQUEST_URI + "/delete/2", null,Result.class, "");
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));
//	}
//
//	@Test
//	public void enableFieldTest(){
//		LOGGER.info("单元测试接口：/base/field/enable");
//		RestTemplate restTemplate  = new RestTemplate();
//		Result result = restTemplate.postForObject(BASE_FIELD_REQUEST_URI + "/enable/2", null,Result.class, "");
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));
//	}
//	
//	@Test
//	public void disableFieldTest(){
//		LOGGER.info("单元测试接口：/base/field/disable");
//		RestTemplate restTemplate  = new RestTemplate();
//		Result result = restTemplate.postForObject(BASE_FIELD_REQUEST_URI + "/disable/2", null,Result.class, "");
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));
//	}
//	
//	@Test
//	public void requiredFieldTest(){
//		LOGGER.info("单元测试接口：/base/field/required");
//		RestTemplate restTemplate  = new RestTemplate();
//		Result result = restTemplate.postForObject(BASE_FIELD_REQUEST_URI + "/required/2", null,Result.class, "");
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));
//	}
//	
//	@Test
//	public void unRequiredFieldTest(){
//		LOGGER.info("单元测试接口：/base/field/unrequired");
//		RestTemplate restTemplate  = new RestTemplate();
//		Result result = restTemplate.postForObject(BASE_FIELD_REQUEST_URI + "/unrequired/2", null,Result.class, "");
//		LOGGER.info("单元测试接口返回数据：" + new Gson().toJson(result));
//	}
//}

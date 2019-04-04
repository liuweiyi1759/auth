package com;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class TestAuth {

	private static String HOST = "http://query.hcassn.com";

	public static void main(String[] args){
		TestAuth.test3();
	}

	/**
	 * 测试鉴权三要素（身份证、姓名、银行卡号）
	 */
	public static void test3() {
		OkHttpClient client = new OkHttpClient();
		//OkHttpClient client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.10.111.70", 3128))).build();
		JSONObject reqJson = new JSONObject();
		JSONObject dataJson = new JSONObject();
		dataJson.put("bizNo", "20181109144735012");// 请求流水号，每次不能重复，长度在18位以内
		dataJson.put("accNo", "6216615300013896854");// 银行卡号
		dataJson.put("certifId", "230606199410201759");// 身份证号
		dataJson.put("customerNm", "刘炜毅");// 姓名
		reqJson.put("v", "1");// 版本号
		reqJson.put("authId", "c01489ef5f4e6712424f340e15c1b2b1");// api授权key(找管理员要)
		reqJson.put("data", dataJson);
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, reqJson.toJSONString());
		Request request = new Request.Builder()
				.url(HOST + "/realAuth/bankCheck3")
				.post(body)
				.addHeader("Content-Type", "application/json")
				.addHeader("cache-control", "no-cache")
				.build();
		try {
			Response response = client.newCall(request).execute();
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void test5() {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost("http://query.hcassn.com/realAuth/bankCheck3");
			JSONObject reqJson = new JSONObject();
			JSONObject dataJson = new JSONObject();
			dataJson.put("bizNo", "20181109144735007");// 请求流水号，每次不能重复，长度在18位以内
			dataJson.put("accNo", "6216615300013896854");// 银行卡号
			dataJson.put("certifId", "230606199410201759");// 身份证号
			dataJson.put("customerNm", "刘炜毅");// 姓名
			reqJson.put("v", "1");// 版本号
			reqJson.put("authId", "4dfa9a94cb723ae744c2afa406d633b3");// api授权key(找管理员要)
			reqJson.put("data", dataJson);
			StringEntity entity = new StringEntity(reqJson.toString(), ContentType.APPLICATION_JSON);
			RequestConfig config = null;
			config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
			httpPost.setConfig(config);
			httpPost.setEntity(entity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			System.out.println(response);
			if (response.getStatusLine().getStatusCode() == 200){
				String resultString = EntityUtils.toString(response.getEntity());
				System.out.println(resultString);
			}else{
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 测试鉴权四要素（身份证、姓名、银行卡号、手机号）
	 */
	public static void test4() {
		OkHttpClient client = new OkHttpClient();
		JSONObject reqJson = new JSONObject();
		JSONObject dataJson = new JSONObject();
		dataJson.put("bizNo", "20181109144735003");// 请求流水号，每次不能重复，长度在18位以内
		dataJson.put("accNo", "6214830177071882");// 银行卡号
		dataJson.put("certifId", "230606199410201759");// 身份证号
		dataJson.put("phoneNo", "15204502500");// 手机号
		dataJson.put("customerNm", "刘炜毅");// 姓名
		reqJson.put("v", "1");// 版本号
		reqJson.put("authId", "4dfa9a94cb723ae744c2afa406d633b3");// api授权key(找管理员要)
		reqJson.put("data", dataJson);
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, reqJson.toJSONString());
		Request request = new Request.Builder()
				.url(HOST + "/realAuth/bankCheck4")
				.post(body)
				.addHeader("Content-Type", "application/json")
				.addHeader("cache-control", "no-cache")
				.build();
		try {
			Response response = client.newCall(request).execute();
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

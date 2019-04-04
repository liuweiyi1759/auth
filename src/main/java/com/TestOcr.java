package com;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Set;
import java.util.UUID;

public class TestOcr {

	public static void main(String[] args) {
            send();
	}

	public static void send(){
		try{
			CloseableHttpClient httpClient = HttpClients.createDefault();
			String random = UUID.randomUUID().toString();
			long time = System.currentTimeMillis() / 1000;
			String appid = "123";
			String appkey = "123";
			String type = "bankcard";
			String img = "data:image/jpg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAgFBgcGBQgHBgcJCAgJDBMMDAsLDBgREg4THB";
			String appCode = "123";
			HttpPost httpPost = new HttpPost("http://10.10.128.32:8558/public-gateway/ocr/v4/ocr/base64?appid=" + appid + "&random=" + random);
			JSONObject clientParamsJson = new JSONObject(true);
			clientParamsJson.put("img", img);
			clientParamsJson.put("time", time);
			clientParamsJson.put("type", type);
			clientParamsJson.put("app_code", appCode);
			clientParamsJson.put("sig", calculateSignature(appkey, random, time, img, type, appCode));
			StringEntity entity = new StringEntity(clientParamsJson.toString(), ContentType.APPLICATION_JSON);
			RequestConfig config = null;
			config = RequestConfig.custom().setConnectTimeout(100000).setSocketTimeout(100000).build();
			httpPost.setConfig(config);
			httpPost.setEntity(entity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200){
				String resultString = EntityUtils.toString(response.getEntity());
				System.out.println(resultString);
			}else{
				System.out.println(response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static String calculateSignature(String appkey, String random, long time, String img, String type, String appCode){
		// 使用com.alibaba.fastjson.JSONObject(版本在1.2.23以上),要求使用true，来保证顺序，注意：appKey和random添加的顺序不要调整
		JSONObject paramsJson = new JSONObject(true);
		paramsJson.put("appkey", appkey);
		paramsJson.put("random", random);
		paramsJson.put("img", img);
		paramsJson.put("time", time);
		paramsJson.put("type", type);
		paramsJson.put("app_code", appCode);
		Set<String> keys = paramsJson.keySet();
		StringBuilder clientParams = new StringBuilder();
		for(String key: keys){
			clientParams.append(key).append("=").append(paramsJson.get(key)).append("&");
		}
		clientParams.deleteCharAt(clientParams.length() - 1);
		String sig = DigestUtils.sha256Hex(clientParams.toString());
		return sig;
	}
}

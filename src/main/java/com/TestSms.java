package com;

import com.alibaba.fastjson.JSONArray;
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

public class TestSms {

    public static void  main(String args[]){
        for(int i =0;i<10;i++){
            send();
        }

    }
    public static void send(){
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String random = UUID.randomUUID().toString();
            long time = System.currentTimeMillis() / 1000;
            JSONArray params = new JSONArray();
            String appid ="test";
            String appkey ="test";
            String sign ="【卡友支付】";
            JSONArray phoneNoArray = new JSONArray();
            phoneNoArray.add("15204502500");
            phoneNoArray.add("15204502501");
            phoneNoArray.add("15204502502");
            phoneNoArray.add("15204502503");
            String phoneNo=phoneNoArray.toString();
            //短信内容：感谢您申请{1}手刷账号，请在app界面输入6位验证码：{2}。
            String tplCode ="BGHKC86E44TH1GB102E0";
            String appCode ="mtpm-appservice";
            HttpPost httpPost = new HttpPost("http://10.10.128.31:8558/public-gateway/public-smsserver/v2/sendsalesms.action?appid="+appid+"&random="+random);
            //使用com.alibaba.fastjson.JSONObject(版本在1.2.23以上),要求使用true，来保证顺序
            JSONObject clientParamsJson = new JSONObject(true);
            clientParamsJson.put("sign",sign);
            clientParamsJson.put("time",time);
            clientParamsJson.put("phone",phoneNo);
            clientParamsJson.put("tpl_code",tplCode);
            clientParamsJson.put("params", params);
            clientParamsJson.put("app_code",appCode);
            clientParamsJson.put("sig", calculateSignature(appkey, random, sign, time, phoneNo, tplCode, appCode, params));
            StringEntity entity = new StringEntity(clientParamsJson.toString(), ContentType.APPLICATION_JSON);
            RequestConfig config = null;
            config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
            httpPost.setConfig(config);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200){
                String resultString = EntityUtils.toString(response.getEntity());
                System.out.println(resultString);
            }else{


            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //获取参数进行加密，如果自行拼接可能服务端加密的顺序不同，从而导致sig校验不通过，所以建议直接采用下面方法即可
    private static String calculateSignature(String appkey, String random, String sign, long time, String phoneNo, String tplCode, String appCode, JSONArray params){
        //使用com.alibaba.fastjson.JSONObject(版本在1.2.23以上),要求使用true，来保证顺序，注意：appKey和random添加的顺序不要调整
        JSONObject paramsJson = new JSONObject(true);
        paramsJson.put("appkey",appkey);
        paramsJson.put("random", random);
        paramsJson.put("sign",sign);
        paramsJson.put("time",time);
        paramsJson.put("phone",phoneNo);
        paramsJson.put("tpl_code",tplCode);
        paramsJson.put("params", params);
        paramsJson.put("app_code",appCode);
        Set<String> keys = paramsJson.keySet();
        StringBuilder clientParams = new StringBuilder();
        for(String key : keys){
            clientParams.append(key).append("=").append(paramsJson.get(key)).append("&");
        }
        clientParams.deleteCharAt(clientParams.length() - 1);
        String sig = DigestUtils.sha256Hex(clientParams.toString());
        return sig;
    }
}

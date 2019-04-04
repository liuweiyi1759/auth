package com;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class TestAuthFront {

	public static void main(String[] args) {
		Map<String, String> paramMap = new HashMap<String, String>();
		String name = "曹慕雅222";
		String cert = "130183199410062404";
		String card = "6217000110003553725";
		paramMap.put("apiCode", "directPay");
		paramMap.put("versionCode", "1.0");
		//参数编码
		paramMap.put("inputCharset", "UTF-8");
		//签名方式
		paramMap.put("signType", "MD5");
		//认证结果后台通知URL
		paramMap.put("notifyURL", "http://www.baidu.com/");
		//8614271579
		//86191111139
		paramMap.put("partner", "8614271579");
		paramMap.put("requestCode", "WHLG1542216230262213211220");
		paramMap.put("busiType", "BINDCARD_AUTH");
		paramMap.put("payerName", name);
		paramMap.put("bankCardNo", card);
		paramMap.put("certNo", cert);
		ArrayList<String> paramNames = new ArrayList<>(paramMap.keySet());
		Collections.sort(paramNames);
		StringBuilder signSource = new StringBuilder();
		Iterator<String> iterator = paramNames.iterator();
		while (iterator.hasNext()) {
			String paramName = iterator.next();
			if (StringUtils.isNoneBlank(paramMap.get(paramName))) {
				signSource.append(paramName).append("=").append(paramMap.get(paramName));
				if (iterator.hasNext())
					signSource.append("&");
			}
		}
		// 签名
		String calSign = null;
		String param = signSource.toString();
		//测试的密钥
		signSource.append("5E1524AABA00627C87DD2E28726AA785");
		try {
			calSign = DigestUtils.md5Hex(signSource.toString().getBytes("UTF-8")).toUpperCase();
		} catch (UnsupportedEncodingException e) {

		}
		System.out.println(calSign);
		//测试地址http://10.10.129.49:8083/auth-front/auth/trade.htm
		//生产地址https://jh.cardinfo.com.cn/auth-front/auth/trade.htm
		String result = sendPost("http://10.10.129.49:8083/auth-front/auth/trade.htm",param+"&sign="+calSign);
		System.out.println(result);
	}

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}

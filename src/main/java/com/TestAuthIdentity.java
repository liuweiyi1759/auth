package com;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TestAuthIdentity {

	public static void main(String[] args) {
		//测试地址：http://10.10.129.49:8083/auth-front/auth/updateIdentityInfo.htm
		//生产地址：https://jh.cardinfo.com.cn/auth-front/auth/updateIdentityInfo.htm
		String certNo = "110101200801015792";
		//String beginDate = "2001-01-01";
		String beginDate = null;
		//String endDate = "2025-01-01";
		String endDate = null;
		String signOrg = "上海市公安局";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("certNo", certNo);
		paramMap.put("beginDate", beginDate);
		paramMap.put("endDate", endDate);
		paramMap.put("signOrg", signOrg);
		ArrayList<String> paramNames = new ArrayList<>(paramMap.keySet());
		Collections.sort(paramNames);
		StringBuilder signSource = new StringBuilder();
		Iterator<String> iterator = paramNames.iterator();
		while (iterator.hasNext()) {
			String paramName = iterator.next();
			if (StringUtils.isNoneBlank(paramMap.get(paramName))) {
				signSource.append(paramName).append("=").append(paramMap.get(paramName));
				if (iterator.hasNext()) {
					signSource.append("&");
				}
			}
		}
		String param = signSource.toString();
		System.out.println(param);
		//字符串后直接拼接密钥，测试和生产都一样
		signSource.append("5E1524AABA00627C87DD2E28726AA785");
		String sign = md5(signSource.toString());
		String result = sendPost("http://10.10.129.49:8083/auth-front/auth/updateIdentityInfo.htm", param + "&sign=" + sign);
		System.out.println(result);
	}

	//java自带的md5
	public static String md5(String plainText) {
		//定义一个字节数组
		byte[] secretBytes = null;
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			//对字符串进行加密
			md.update(plainText.getBytes());
			//获得加密后的数据
			secretBytes = md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		//将加密后的数据转换为16进制数字
		String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
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

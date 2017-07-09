package com.example.ubuntu.qc_scanner.http;

import com.example.ubuntu.qc_scanner.manager.RequestManager;
import com.example.ubuntu.qc_scanner.mode.AnalyticalRegistInfo;
import com.example.ubuntu.qc_scanner.mode.UserBaseInfo;
import com.example.ubuntu.qc_scanner.util.MD5Util;

import java.util.HashMap;
 
/*
 * 网络接口 wjl
 */
/**
 * @author Administrator
 *
 */
public class RequestApiData {
	private static RequestApiData instance = null;
	private HttpResponeCallBack mCallBack = null;

	//创建接口对象
	public static RequestApiData getInstance() {
		if (instance == null) {
			instance = new RequestApiData();
		}
		return instance;
	}

	/**
	 * 4.6注册用户接口
	 * @param nickname  昵称
	 * @param email 邮箱
	 * @param password 密码
	 * @param clazz  数据返回的解析对象
	 * @param callback 回调
	 * 特别要注意参数位置不能变要根据文档来
	 * 请求方式：POST
	 */
	public void getRegistData(String nickname, String email
			, String password, Class<AnalyticalRegistInfo> clazz,
							  HttpResponeCallBack callback) {
		mCallBack = callback;
		//这是每一个接口的唯一标示
		String tagUrl = UrlConstance.KEY_REGIST_INFO;//注册接口
		//将注册的信息保存在map中（须和服务器端一致）
		HashMap<String, String> parameter = new HashMap<String, String>();
		parameter.put("nickname", nickname);
		parameter.put("email", email);
		parameter.put("password", password);

		//拼接参数信息，昵称，邮箱，密码，公钥，并用md5进行加密
		StringBuilder builder = new StringBuilder();
		builder.append(nickname);
		builder.append(email);
		builder.append(password);
		builder.append(UrlConstance.PUBLIC_KEY);

		parameter.put(UrlConstance.ACCESSTOKEN_KEY, MD5Util.getMD5Str(builder.toString()));

		//请求数据接口
		RequestManager.post(UrlConstance.APP_URL, tagUrl, parameter, clazz, callback);

	}


	/**
	 * 4.8登录用户接口
	 * @param email 邮箱
	 * @param password 密码
	 * @param clazz 数据返回的解析对象
	 * @param callback 回调
	 * 特别要注意参数位置不能变要根据文档来
	 * 请求方式：POST
	 */
	public void getLoginData(String email, String password,
							 Class<UserBaseInfo> clazz,
							 HttpResponeCallBack callback) {
		mCallBack = callback;
		//这是每一个接口的唯一标示
		String tagUrl = UrlConstance.KEY_LOGIN_INFO;//登录接口
		HashMap<String, String> parameter = new HashMap<String, String>();
		parameter.put("email", email);
		parameter.put("password", password);

		//拼接参数信息，邮箱，密码，公钥，并用md5进行加密
		StringBuilder builder = new StringBuilder();
		builder.append(email);
		builder.append(password);
		builder.append(UrlConstance.PUBLIC_KEY);

		parameter.put(UrlConstance.ACCESSTOKEN_KEY, MD5Util.getMD5Str(builder.toString()));

		//请求数据接口
		RequestManager.post(UrlConstance.APP_URL, tagUrl, parameter, clazz, callback);
	}
}

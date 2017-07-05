package com.example.ubuntu.qc_scanner.application;

import com.example.ubuntu.qc_scanner.http.RequestApiData;
import com.example.ubuntu.qc_scanner.mode.UserBaseInfo;

/**
 * @author wjl IT蓝豹 ItLanBaoApplication主要作用是处理一些app全局变量，
 */
public class ItLanBaoApplication extends ItLanbaoLibApplication {

	private UserBaseInfo baseUser;//用户基本信息

	private RequestApiData requestApi;
	private static ItLanBaoApplication instance;

	// 渠道号
	private String fid = "";
	// 版本号
	private String version = "";

	@Override
	public void onCreate() {
		super.onCreate();
		setInstance(this);
		requestApi = RequestApiData.getInstance();
	}

	public static void setInstance(ItLanBaoApplication instance) {
		ItLanBaoApplication.instance = instance;
	}

	/**
	 * 设置用户基本信息
	 * @param baseUser
	 */
	public void setBaseUser(UserBaseInfo baseUser) {
		this.baseUser = baseUser;
	}

	/**
	 * 获取ItLanBaoApplication实例
	 *
	 * @return
	 */
	public static ItLanBaoApplication getInstance() {
		return instance;
	}
}

package com.example.ubuntu.qc_scanner.application;

import com.mob.MobApplication;

public class ItLanbaoLibApplication extends MobApplication {
	
	private static ItLanbaoLibApplication instance;

	@Override
	public void onCreate() {
		super.onCreate(); 
		setInstance(this); 
      
	} 
	
	public static void setInstance(ItLanbaoLibApplication instance) {
		ItLanbaoLibApplication.instance = instance;
	}
	
	/** 
	 * 获取时间戳
	 * @return
	 */
	public String getTime(){
		return String.valueOf(System.currentTimeMillis());
	}

	/**
	 * 获取ItLanBaoApplication实例
	 * 
	 * @return
	 */
	public static ItLanbaoLibApplication getInstance() {
		return instance;
	}
 
}

package com.example.ubuntu.qc_scanner.mode;

import java.io.Serializable;

/**
 * @author itlanbao
 * IT蓝豹
 * 解析注册接口数据
 */
public class AnalyticalRegistInfo implements Serializable{
	private String ret;//0 ：数据返回成功 1 ：数据返回失败 
	private String errcode;// errcode错误码类型
	private String msg;//错误信息
	private String nickname;//昵称
	private String userhead;//用户头像路径
	private String email;//用户邮件
	private int userid;//用户id

	
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUserhead() {
		return userhead;
	}
	public void setUserhead(String userhead) {
		this.userhead = userhead;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	

}

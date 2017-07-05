package com.example.ubuntu.qc_scanner.mode;

import java.io.Serializable;

/**
 * @author itlanbao
 * IT蓝豹
 * 解析获取用户基本信息
 */
public class UserBaseInfo implements Serializable{

//	 {"ret":"0","errcode":"0","msg":"接口调用成功","nickname":"erom","userhead":"/img/users/head/avatar.png",
//	"userid":"11653","email":"123456789@qq.com","role":"0"}
	private String userid;//用户id
	private String nickname;//昵称
	private String userhead;//用户头像路径 
	private String email;//用户邮件
	private String ret;//请求状态码
	private String errcode;//错误码
	private String msg;
	private String role;//角色 是不是管理员

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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
 

}

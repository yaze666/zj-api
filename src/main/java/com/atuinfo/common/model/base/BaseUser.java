package com.atuinfo.common.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public void setUserId(Integer userId) {
		set("user_id", userId);
	}

	public Integer getUserId() {
		return getInt("user_id");
	}

	public void setAccount(String account) {
		set("account", account);
	}

	public String getAccount() {
		return getStr("account");
	}

	public void setPassword(String password) {
		set("password", password);
	}

	public String getPassword() {
		return getStr("password");
	}

	public void setUsername(String username) {
		set("username", username);
	}

	public String getUsername() {
		return getStr("username");
	}

	public void setRegTime(java.util.Date regTime) {
		set("reg_time", regTime);
	}
	
	public java.util.Date getRegTime() {
		return get("reg_time");
	}

}

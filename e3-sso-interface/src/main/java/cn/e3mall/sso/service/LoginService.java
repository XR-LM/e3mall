package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;

public interface LoginService {
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	E3Result userLogin(String username, String password);
}

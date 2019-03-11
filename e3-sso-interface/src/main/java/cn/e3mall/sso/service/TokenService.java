package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;

public interface TokenService {
	/**
	 * 通过token查询用户信息
	 * @param token
	 * @return
	 */
	E3Result getUserByToken(String token);
}

package cn.e3mall.sso.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbUser;

@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result getUserByToken(String token) {
		//根据token查询redis数据库中对应的用户信息是否过期
		String string = jedisClient.get("SESSION:"+token);
		if(StringUtils.isBlank(string))
			return E3Result.build(201, "用户信息已过期");
		//更新过期时间
		jedisClient.expire("SESSION:"+token,SESSION_EXPIRE );
		//获取用户对象
		TbUser user = JsonUtils.jsonToPojo(string, TbUser.class);
		return E3Result.ok(user);
	}

}

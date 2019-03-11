package cn.e3mall.sso.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result userLogin(String username, String password) {
		//判断用户名密码是否为空
		if(StringUtils.isBlank(username)||StringUtils.isBlank(password)) {
			return E3Result.build(400, "用户名或密码为空");
		}
		//根据用户名查询数据库
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list==null||list.size()==0)
			return E3Result.build(400, "用户名或密码错误");
		
		TbUser user=list.get(0);
		//校验密码
		if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
			return E3Result.build(400, "用户名或密码错误");
		}
		
		//模拟Session
		//生成token相当于sessin的UUID
		String token = UUID.randomUUID().toString();
		//将密码设为空
		user.setPassword(null);
		//将用户信息保存到redis集群中
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		//设置用户信息过期时间
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		
		// 6、把token返回
		 return E3Result.ok(token);
	}

}

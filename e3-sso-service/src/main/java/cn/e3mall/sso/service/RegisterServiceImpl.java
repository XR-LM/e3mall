package cn.e3mall.sso.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;

@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	private TbUserMapper userMapper;
	@Override
	public E3Result checkData(String param, int type) {
		//创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//根据type类型设置查询条件
		if(type==1) {
			criteria.andUsernameEqualTo(param);
		}else if (type==2) {
			criteria.andPhoneEqualTo(param);
		}else {
			return E3Result.build(400, "参数错误");
		}
		
		//执行查询
		List<TbUser> userlist = userMapper.selectByExample(example);
		//取查询结果
		if(userlist!=null&&userlist.size()>0) {
			return E3Result.ok(false);
		}
			
		return E3Result.ok(true);
		
	}

	@Override
	public E3Result register(TbUser user) {
		// 验证数据是否合法
		if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())||StringUtils.isBlank(user.getPhone())) {
			return E3Result.build(400, "用户数据不完整，注册失败");
		}
		//1：用户名 2：手机号 3：邮箱
		E3Result result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return E3Result.build(400, "此用户名已经被占用");
		}
		result = checkData(user.getPhone(), 2);
		if (!(boolean)result.getData()) {
			return E3Result.build(400, "手机号已经被占用");
		}
		//补全pojo的属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//对密码进行md5加密
		String digestAsHex = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(digestAsHex);
		userMapper.insert(user);
		return E3Result.ok();
		
	}

}

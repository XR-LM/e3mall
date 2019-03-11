package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbUser;

public interface RegisterService {
	/**
	 * 检查数据是否合法
	 * @param param 需要校验的数据
	 * @param type 数据类型 1、2、3分别代表username、phone、email
	 * @return
	 */
	E3Result checkData(String param, int type);
	E3Result register(TbUser user);
}

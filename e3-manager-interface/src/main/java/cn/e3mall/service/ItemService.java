package cn.e3mall.service;

import cn.e3mall.pojo.TbItem;

public interface ItemService {
	/*
	 * 根据id查询商品信息
	 */
	TbItem getItemById(long id) ;
}

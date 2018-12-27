package cn.e3mall.service;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.page.EasyUIDataGridResult;

public interface ItemService {
	/*
	 * 根据id查询商品信息
	 */
	TbItem getItemById(long id) ;
	/**
	 * 分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGridResult getItemList(int page, int rows);
}

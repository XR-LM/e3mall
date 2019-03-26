package cn.e3mall.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;

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
	/**
	 * 添加商品
	 * @param item
	 * @param desc
	 * @return
	 */
	E3Result addItem(TbItem item,String desc);
	/**
	 * 查询商品详情
	 * @param id
	 * @return
	 */
	E3Result findItemDescById(Long id);
	/**
	 * 查询商品表格参数
	 * @param id
	 * @return
	 */
	E3Result findItemParamById(Long id);
	/**
	 * 更新商品信息
	 * @param item
	 * @param desc
	 * @return
	 */
	E3Result updateItem(TbItem item,String desc);
}

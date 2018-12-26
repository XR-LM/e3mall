package cn.e3mall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	/*
	 * 根据id查询商品
	 * @see cn.e3mall.service.ItemService#getItemById(long)
	 */
	@Override
	public TbItem getItemById(long id) {
		// TODO 自动生成的方法存根
		TbItem item= itemMapper.selectByPrimaryKey(id);
		return item;
	}

}

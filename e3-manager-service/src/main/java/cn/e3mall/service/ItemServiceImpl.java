package cn.e3mall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.page.EasyUIDataGridResult;

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

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 从page页开始，每页rows条数据,该方法会自动查询所有数据
		PageHelper.startPage(page, rows);
		//设置查询条件
		TbItemExample example = new TbItemExample();
		//默认查询所有
		List<TbItem> list=itemMapper.selectByExample(example);
		//取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) pageInfo.getTotal());
		result.setRows(list);
		
		return result;
	}

}

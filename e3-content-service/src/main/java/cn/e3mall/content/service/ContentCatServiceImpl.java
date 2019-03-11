package cn.e3mall.content.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCatServiceImpl implements ContentCatService{
	
	@Autowired
	private TbContentCategoryMapper contentCatMapper;
	@Override
	public List<EasyUITreeNode> findContentCat(Long parentId) {
		// 创建查询对象
		TbContentCategoryExample example = new TbContentCategoryExample();
		//设置离线查询对象
		Criteria criteria = example.createCriteria();
		//设置查询参数
		criteria.andParentIdEqualTo(parentId);
		criteria.andStatusEqualTo(1);
		//执行查询
		List<TbContentCategory> list = contentCatMapper.selectByExample(example);
		List<EasyUITreeNode> result= new ArrayList<EasyUITreeNode>();	
		//将结果转存为List<EasyUIreeNode>类型
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			result.add(node);
		}
		return result;
	}
	@Override
	public E3Result addContentCat(Long parentId, String name) {
		
		//创建结点对象补充相应属性
		TbContentCategory contentCategory = new TbContentCategory();
		
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		contentCategory.setIsParent(false);
		//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		contentCategory.setSortOrder(1);
		//状态。可选值:1(正常),2(删除)
		contentCategory.setStatus(1);
		Date date = new Date();
		contentCategory.setCreated(date);
		contentCategory.setUpdated(date);
		// c)向tb_content_category表中插入数据
		contentCatMapper.insert(contentCategory);
		// 3、判断父节点的isparent是否为true，不是true需要改为true。
		TbContentCategory parentNode = contentCatMapper.selectByPrimaryKey(parentId);
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			//更新父节点
			contentCatMapper.updateByPrimaryKey(parentNode);
		}
		// 4、需要主键返回。
		// 5、返回E3Result，其中包装TbContentCategory对象
		return E3Result.ok(contentCategory);

	}
	@Override
	public E3Result reName(Long id, String name) {
		// 查询出相应节点
		TbContentCategory contentCategory = contentCatMapper.selectByPrimaryKey(id);
		//设置节点信息
		contentCategory.setName(name);
		//提交更新
		contentCatMapper.updateByPrimaryKey(contentCategory);
		return E3Result.ok();
	}
	@Override
	public E3Result deleteContentCat(Long id) {
		// 查询节点
		TbContentCategory contentCategory = contentCatMapper.selectByPrimaryKey(id);
		//判断节点是否是父节点
		if(contentCategory.getIsParent())
		{
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(id);
			criteria.andStatusEqualTo(1);
			List<TbContentCategory> list = contentCatMapper.selectByExample(example);
			if(list.isEmpty()) {
				contentCategory.setStatus(2);
				contentCatMapper.updateByPrimaryKey(contentCategory);
				return E3Result.ok();
			}else {
				return E3Result.build(404,"请先删除叶子节点");
			}
		}else {
			
				contentCategory.setStatus(2);
				contentCatMapper.updateByPrimaryKey(contentCategory);
				return E3Result.ok();
		}
	}

}

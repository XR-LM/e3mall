package cn.e3mall.content.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private TbContentMapper contentMapper;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public EasyUIDataGridResult findContentList(int page, int rows, Long id) {
		// 从page页开始，每页rows条数据,该方法会自动查询所有数据
		PageHelper.startPage(page, rows);
		//设置查询条件
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(id);
		//默认查询所有
		List<TbContent> list=contentMapper.selectByExample(example);
		//取分页信息
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		//创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) pageInfo.getTotal());
		result.setRows(list);
		
		return result;
	}
	@Override
	public E3Result addContent(TbContent content) {
		// 补全信息
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		//缓存同步
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());

		return E3Result.ok();
	}
	@Override
	public List<TbContent> getContentListByCid(Long cid) {
		// 查询缓存中的数据
		try {
			//如果存在就直接返回数据
			String json = jedisClient.hget(CONTENT_LIST, cid+"");
			//判断json是否为空
			if(StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExample(example);
		//向缓存中添加数据
		try {
			jedisClient.hset(CONTENT_LIST, cid + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(!list.isEmpty())
			return list;
		return null;
	}

}

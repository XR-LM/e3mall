package cn.e3mall.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.IDUtils;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.mapper.TbItemParamMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.pojo.TbItemParam;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	private TbItemParamMapper itemParamMapper;
	
	@Autowired
	private JmsTemplate jmstemplate;
	
	@Resource
	private Destination topicDestination;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	
	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;
	
	
	/*
	 * 根据id查询商品
	 * @see cn.e3mall.service.ItemService#getItemById(long)
	 */
	@Override
	public TbItem getItemById(long itemId) {
		//将查询到的商品添加到缓存中
		//为了不影响程序的正常执行应该将缓存处理放入try-catch块中
		try {
			String string = jedisClient.get(ITEM_INFO_PRE+":"+itemId+":BASE");
			if(StringUtils.isNotBlank(string)) {
				TbItem tbItem =JsonUtils.jsonToPojo(string, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//根据商品id查询商品信息
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			try {
				//把数据保存到缓存
				jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
				//设置缓存的有效期
				jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":BASE", ITEM_INFO_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return item;
		}
		return null;

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

	@Override
	public E3Result addItem(TbItem tbItem, String desc) {
		// TODO 自动生成的方法存根
		//生成商品ID
		Long id=IDUtils.genItemId();
		tbItem.setId(id);
		//商品状态，1-正常，2-下架，3-删除
		tbItem.setStatus((byte) 1);
		//商品日期
		Date date = new Date();
		tbItem.setCreated(date);
		tbItem.setUpdated(date);
		
		itemMapper.insert(tbItem);
		//补充ITem_Desc
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(id);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		// 6、向商品描述表插入数据
		itemDescMapper.insert(tbItemDesc);
		//发送一个商品添加信息
		jmstemplate.send(topicDestination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(id + "");
				return textMessage;
			}
		});
		
		// 7、E3Result.ok()
		return E3Result.ok();
	}

	@Override
	public E3Result findItemDescById(Long id) {
		try {
			String string = jedisClient.get(ITEM_INFO_PRE+":"+id+":DESC");
			if(StringUtils.isNotBlank(string)) {
				TbItemDesc tbItemDesc =JsonUtils.jsonToPojo(string, TbItemDesc.class);
				return E3Result.ok(tbItemDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc=itemDescMapper.selectByPrimaryKey(id);
		if(itemDesc!=null) {
			try {
				//把数据保存到缓存
				jedisClient.set(ITEM_INFO_PRE + ":" + id + ":DESC", JsonUtils.objectToJson(itemDesc));
				//设置缓存的有效期
				jedisClient.expire(ITEM_INFO_PRE + ":" + id + ":DESC", ITEM_INFO_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return E3Result.ok(itemDesc);
		}
		else {
			return E3Result.build(404, "暂无详情信息");
		}
	}

	@Override
	public E3Result findItemParamById(Long id) {
		// TODO 自动生成的方法存根
		TbItemParam itemParam=itemParamMapper.selectByPrimaryKey(id);
		if(itemParam!=null)
			return E3Result.ok(itemParam);
		else {
			return E3Result.build(404, "暂无详情信息");
		}
	}

	@Override
	public E3Result updateItem(TbItem tbItem, String desc) {
		// TODO 自动生成的方法存根
		TbItem  oldItem=itemMapper.selectByPrimaryKey(tbItem.getId());
		//商品日期
		Date date = new Date();
		tbItem.setStatus(oldItem.getStatus());
		tbItem.setCreated(oldItem.getCreated());
		tbItem.setUpdated(date);
		
		itemMapper.updateByPrimaryKey(tbItem);
		//补充ITem_Desc
		TbItemDesc tbItemDesc =itemDescMapper.selectByPrimaryKey(oldItem.getId());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setUpdated(date);
		// 6、向商品描述表插入数据
		itemDescMapper.updateByPrimaryKey(tbItemDesc);
		// 7、E3Result.ok()
		return E3Result.ok();
	}
	
}

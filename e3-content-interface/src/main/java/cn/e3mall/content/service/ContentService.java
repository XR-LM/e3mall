package cn.e3mall.content.service;
/**
 * 页面内容控制Service
 * @author XR
 *
 */

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	/**
	 * 根据id查询内容列表带分页
	 * @param id
	 * @return
	 */
	EasyUIDataGridResult findContentList(int page,int rows,Long id);
	/**
	 * 根据分类id 查询分类列表
	 * @param cid
	 * @return
	 */
	List<TbContent> getContentListByCid(Long cid);
	/**
	 * 添加分类信息
	 * @param content
	 * @return
	 */
	E3Result addContent(TbContent content);
}

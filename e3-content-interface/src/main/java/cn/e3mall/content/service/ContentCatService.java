package cn.e3mall.content.service;
/**
 * 内容列表接口
 * @author XR
 *
 */

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ContentCatService {
	/**
	 * 通过父结点ID查询节点
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> findContentCat(Long parentId);
	/**
	 * 添加结点
	 * @param parentId
	 * @param name
	 * @return
	 */
	E3Result addContentCat(Long parentId,String name);
	/**
	 * 节点重命名
	 * @param id
	 * @param name
	 * @return
	 */
	E3Result reName(Long id,String name);
	/**
	 * 根据id删除节点
	 * @param id
	 * @return
	 */
	E3Result deleteContentCat(Long id);
	
}

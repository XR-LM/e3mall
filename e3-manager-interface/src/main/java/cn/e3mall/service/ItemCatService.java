package cn.e3mall.service;
/**
 * 商品类别
 * @author XR
 *
 */

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	List<EasyUITreeNode> getCatList(Long parentId);
}

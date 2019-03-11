package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

/**
 * 搜索商品接口
 * @author XR
 *
 */
public interface SearchService {
	/**
	 * 查询商品，带翻页
	 * @param keyWord
	 * @param page
	 * @param rows
	 * @return
	 */
	SearchResult search(String keyWord, int page, int rows);
}

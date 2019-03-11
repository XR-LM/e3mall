package cn.e3mall.search.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchItemDao;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchItemDao searchItemDao;
	
	@Value("${DEFAULT_FIELD}")
	private String DEFAULT_FIELD;
	
	@Override
	public SearchResult search(String keyWord, int page, int rows) {
		// 创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery(keyWord);
		//设置分页查询条件
		query.setStart((page-1)*rows);
		query.setRows(rows);
		//设置默认搜索域
		query.set("df", DEFAULT_FIELD);
		//设置高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//执行查询
		SearchResult searchResult = null;
		try {
			searchResult = searchItemDao.search(query);
			//计算总页数
			int recourdCount=searchResult.getRecourdCount();
			int pages=(int) Math.ceil(recourdCount/rows);
			//设置到返回结果中
			searchResult.setTotalPages(pages);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return searchResult;
	}

}

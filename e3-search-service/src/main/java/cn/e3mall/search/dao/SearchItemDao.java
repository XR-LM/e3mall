package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchItemService;

@Repository
public class SearchItemDao {
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery query) throws Exception {
		//根据查询条件查询索引库
		QueryResponse response = solrServer.query(query);
		//取查询结果总记录数
		SolrDocumentList solrDocumentList = response.getResults();
		long numFound = solrDocumentList.getNumFound();
		//创建一个返回结果对象
		SearchResult searchResult = new SearchResult();
		searchResult.setRecourdCount((int)numFound);
		
		//创建一个商品列表对象
		List<SearchItem> list = new ArrayList<>();
		//取高亮结果
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		//填充结果集
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem searchItem = new SearchItem();
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			//取高亮结果
			List<String> list2 = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle="";
			if(list2!=null&&list2.size()>0) {
				itemTitle=list2.get(0);
			}else {
				itemTitle=(String) solrDocument.get("item_title");
			}
			
			searchItem.setTitle(itemTitle);
			
			//添加到商品列表
			list.add(searchItem);
		
		}
		//把列表添加到结果对象中
		searchResult.setItemList(list);
		return searchResult;
		
	}
}

package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	/**
	 * 添加数据
	 * @throws Exception
	 */
	@Test
	public void Document() throws Exception{
		//创建一个SolrServer对象，创建一个连接，参数solr服务的url
		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.16:8080/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域。文档中必须包含一个id域，所有的域名城必须在schema.xml中定义过
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 1000);
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
		
	}
	/**
	 * 查询数据
	 */
	public void queryIndex() throws Exception{
		//创建SolrServer对象
		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.16:8080/solr/collection1");
		//创建查询对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.set("q", "*:*");
		//执行查询，得到QueryResponse对象
		QueryResponse queryResponse =solrServer.query(query);
		//获取文档列表，取查询记录数
		SolrDocumentList list = queryResponse.getResults();
		System.out.println("查询的结果总记录数："+list.getNumFound());
		//遍历文档列表，获取域中的内容
		for (SolrDocument solrDocument : list) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
		
	}
}

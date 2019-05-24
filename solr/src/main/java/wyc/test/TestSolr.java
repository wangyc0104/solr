package wyc.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * maven整合spring与solr
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSolr {
	@Autowired
	HttpSolrServer httpSolrServer;

	@Test
	public void testInsertData() throws IOException, SolrServerException {

		// 循环地导入100个数据
//		for(int i = 0; i<100;i++){
//			SolrInputDocument sid = new SolrInputDocument();
//			sid.addField("id", i+"kiki"+i);
//			sid.addField("name", "Yicheng"+i+"kiki"+i);
//			httpSolrServer.add(sid);
//		}
//		httpSolrServer.commit();
		
		// 导入单个数据
//		SolrInputDocument solrInputDocument = new SolrInputDocument();
//		solrInputDocument.addField("id", "10086");
//		solrInputDocument.addField("name", "Yicheng");
//
//		httpSolrServer.add(solrInputDocument);
//		httpSolrServer.commit();
	}

	@Test
	public void testUpdateData() throws IOException, SolrServerException {

		SolrInputDocument solrInputDocument = new SolrInputDocument();
		solrInputDocument.addField("id", "10086");
		solrInputDocument.addField("product_price", "998888");
		solrInputDocument.addField("product_name", "大黄音乐555");

		httpSolrServer.add(solrInputDocument);
		httpSolrServer.commit();
	}

	@Test
	public void testDelete() throws IOException, SolrServerException {

		// httpSolrServer.deleteById("10086");
		httpSolrServer.deleteByQuery("name:*Yicheng*");
		httpSolrServer.commit();

	}

	@Test
	public void testQuery() throws SolrServerException {

		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("product:挂钩");
		QueryResponse queryResponse = httpSolrServer.query(solrQuery);

		SolrDocumentList results = queryResponse.getResults();
		for (SolrDocument result : results) {
			Collection<String> fieldNames = result.getFieldNames();
			for (String fieldName : fieldNames) {

				System.out.println(fieldName + "  ---->  " + result.get(fieldName));
			}
		}
	}

	@Test
	public void testQuery2() throws SolrServerException {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("product_name:家居");
		solrQuery.setFields("product_name,product_price");
		solrQuery.setFilterQueries("product_price:[10 TO 100]");
		solrQuery.setSort("product_price", SolrQuery.ORDER.asc);
		solrQuery.setHighlight(true);
		solrQuery.set("hl.fl", "product_name");
		QueryResponse queryResponse = httpSolrServer.query(solrQuery);

		SolrDocumentList results = queryResponse.getResults();

		System.out.println("查询回来的数量：" + results.size());
		for (SolrDocument result : results) {
			Collection<String> fieldNames = result.getFieldNames();
			for (String fieldName : fieldNames) {

				System.out.println(fieldName + "  ---->  " + result.get(fieldName));
				System.out.println("------------------------------------");
			}
		}
		// 获取高亮数据：
		System.out.println("获取高亮数据：");
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		Set<String> set1 = highlighting.keySet();
		for (String key1 : set1) {
			System.out.println("key1---->" + key1);
			Map<String, List<String>> map2 = highlighting.get(key1);
			System.out.println("map2 = " + map2);
			Set<String> set2 = map2.keySet();
			for (String key2 : set2) {
				System.out.println("key2---->" + key2);
			}
		}
	}
}
package elasticsearch;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello");	
		//deleteIndex();
		createIndex();

	}
	
	public static void createIndex() {
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));
		
		String INDEX_NAME="movie_rest";
		String TYPE_NAME="_doc";

		/* JSON을 만들어준다.			  
		  {
		  "mappings": {
		    "_doc": {
		      "properties": {
		        "movieCd": {
		          "type": "keyword",
		          "store": true
		        },
		        "movieNm": {
		          "type": "text",
		          "store": true,
		          "index_options": "docs"
		        }
		      }
		    }
		  }
		}	
		*/
		XContentBuilder indexBuilder = null;
		try {
			indexBuilder = XContentFactory.jsonBuilder()
					.startObject()
						.startObject("properties")
							.startObject("movieCd")
								.field("type", "keyword")
								.field("store", "true")
								.field("index_options", "docs")
							.endObject()
							.startObject("movieNm")
								.field("type", "text")
								.field("store", "true")
								.field("index_options", "docs")
							.endObject()
						.endObject()						
					.endObject();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//매핑 설정
		CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
		request.mapping(indexBuilder);
		
		//별칭 설정
		String ALIAS_NAME = "movie_auto_alias";
		request.alias(new Alias(ALIAS_NAME));

		CreateIndexResponse createIndexResponse = null;
		//인덱스 생성
		try {
			createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean acknowledged = createIndexResponse.isAcknowledged();
		//client.indices().createAsync(request, RequestOptions.DEFAULT, listener);	
		
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void deleteIndex() {
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));

		String INDEX_NAME = "movie_rest";
		
		DeleteIndexRequest request = new DeleteIndexRequest(INDEX_NAME);
		
		try {
			AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
			boolean acknowledged = deleteIndexResponse.isAcknowledged();
			if(acknowledged) System.out.println("deleted");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

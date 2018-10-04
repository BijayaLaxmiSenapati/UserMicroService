package com.bridgelabz.fundoo.user.repositories;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.user.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class UserRepositoryESImpl implements UserRepositoryES {

	private final String INDEX = "userindex";
	private final String TYPE = "Users";

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@Autowired
	private SearchSourceBuilder searchSourceBuilder;
	
	@Autowired
	private ObjectMapper objectMapper;

	public User save(User user) {
		Map<?, ?> dataMap = objectMapper.convertValue(user, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, user.getId()).source(dataMap);
		try {
			IndexResponse response = restHighLevelClient.index(indexRequest);
		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}
		return user;
	}

	public Optional<User> findByEmail(String emailId)  {

		/*
		SearchRequest request = new SearchRequest(INDEX);
		
		request.types(TYPE);
		 QueryBuilder queryBuilder = QueryBuilders.matchQuery("email", emailId);
		
		searchSourceBuilder.query(queryBuilder);
		request.source(searchSourceBuilder);

		SearchResponse searchResponse=null;
		Optional<User> optionalUser = null;*/
		
		 SearchRequest request = new SearchRequest(INDEX);
		 request.types(TYPE);

	        QueryBuilder queryBuilder = QueryBuilders.matchQuery("email", emailId);
	        searchSourceBuilder.query(queryBuilder);

	        request.source(searchSourceBuilder);

	      //  SearchHits hits = searchResponse.getHits();
	    	Optional<User> optionalUser = null;
		try {
			
			
			  SearchResponse searchResponse = restHighLevelClient.search(request);
		//	searchResponse = restHighLevelClient.search(request);
		
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			
			for (SearchHit hit : searchHits) {
				optionalUser = Optional.of(objectMapper.readValue(hit.getSourceAsString(), User.class));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return optionalUser;
	}

	public Optional<User> findById(String id) {
		GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
		GetResponse getResponse = null;
		Optional<User> optionalUser = null;
		System.out.println(id);
		try {
			getResponse = restHighLevelClient.get(getRequest);
			String userData = getResponse.getSourceAsString();
			System.out.println(userData);
			optionalUser = Optional.of(objectMapper.readValue(userData, User.class));
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
		return optionalUser;
	}

}

package com.bridgelabz.fundoo.user.repositories;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class RedisRepositoryImplementation  implements RedisRepository  {

	@Value("${redis.key}")
	private String key;
	
	    private RedisTemplate<String, String> redisTemplate;
	  
	    private HashOperations<String, String, String> hashOperations;
	  
	    @Autowired
	    public RedisRepositoryImplementation(RedisTemplate<String, String> redisTemplate) {
	        this.redisTemplate = redisTemplate;
	    }
	  
	    @PostConstruct
	    private void init() {
	        hashOperations = redisTemplate.opsForHash();
	    }
	 
	    @Override
	    public void save(String uuid, String userId) {
	        hashOperations.put(key, uuid, userId);
	    }
	 
	    @Override
	    public String find(String uuid) {
	        return hashOperations.get(key, uuid);
	    }

		@Override
		public void delete(String uuid) {
			hashOperations.delete(key, uuid);
		}
}

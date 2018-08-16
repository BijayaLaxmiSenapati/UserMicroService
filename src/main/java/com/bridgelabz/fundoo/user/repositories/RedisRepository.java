package com.bridgelabz.fundoo.user.repositories;

public interface RedisRepository {

	public String find(String uuid);

	public void save(String uuid, String userId);

	public void delete(String uuid);
}

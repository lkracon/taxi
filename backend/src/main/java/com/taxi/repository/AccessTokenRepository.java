package com.taxi.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taxi.model.AccessToken;
import com.taxi.model.User;


public interface AccessTokenRepository  extends JpaRepository<AccessToken, Long> {

	List<AccessToken> findByUserOrderByExpireDesc(User user);
	
	AccessToken findByToken(String token);
	
	List<AccessToken> findByUser(User user);
}
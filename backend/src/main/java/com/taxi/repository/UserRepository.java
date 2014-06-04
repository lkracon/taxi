package com.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taxi.model.User;


public interface UserRepository  extends JpaRepository<User, Long> {
	
	User findOneByLogin(String login); 
	
}
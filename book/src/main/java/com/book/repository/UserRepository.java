package com.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.model.User;


public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUsername(String username);

}

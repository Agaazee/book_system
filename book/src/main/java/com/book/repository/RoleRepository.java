package com.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

}

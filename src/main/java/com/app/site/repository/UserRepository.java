package com.app.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.site.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
}

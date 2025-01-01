package com.interland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interland.dto.Users;


@Repository
public interface UserRepository extends JpaRepository<Users,String> {

}

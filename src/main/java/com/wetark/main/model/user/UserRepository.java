package com.wetark.main.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByPhoneNo(String phoneNo);
  Boolean existsByPhoneNo(String phoneNo);
  Boolean existsByEmail(String email);
}

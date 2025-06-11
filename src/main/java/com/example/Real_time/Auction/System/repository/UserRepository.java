package com.example.Real_time.Auction.System.repository;

import com.example.Real_time.Auction.System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
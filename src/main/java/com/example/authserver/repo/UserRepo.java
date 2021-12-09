package com.example.authserver.repo;


import com.example.authserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<User,Long> {

    User findByEmail(String email);
}

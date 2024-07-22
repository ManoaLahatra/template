package com.api.template.repository;

import org.springframework.stereotype.Repository;

import com.api.template.models.auth.User;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Integer> {
    User findUserById(int id);
    Optional<User> findUserByEmail(String email);
}
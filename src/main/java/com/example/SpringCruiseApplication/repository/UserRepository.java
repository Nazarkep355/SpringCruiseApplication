package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findUserByEmail(String email);
}

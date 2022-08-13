package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public User getUserByEmail(String email){
        return userRepository.findUserByEmail(email).get();
    }
}

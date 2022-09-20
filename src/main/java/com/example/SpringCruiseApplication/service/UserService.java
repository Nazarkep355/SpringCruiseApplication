package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@Service
@Validated
public class UserService {
    @Autowired
    UserRepository userRepository;
    public User getUserByEmail(String email){
        return userRepository.findUserByEmail(email).get();
    }

    public User registerUser(@Valid User user) {
        if(userRepository.findUserByEmail(user.getEmail()).isPresent()){
            throw new IllegalArgumentException("error.emailInUse");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        user.setMoney(0);
        return userRepository.save(user);
    }
    public void changeBalance(User user, int money){
        User dbUser = userRepository.findUserByEmail(user.getEmail()).get();
        dbUser.setMoney(money);
        userRepository.save(dbUser);
    }
}

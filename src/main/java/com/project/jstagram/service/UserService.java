package com.project.jstagram.service;

import com.project.jstagram.model.User;
import com.project.jstagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public void createUser(User user){
        this.userRepository.save(user);
    }
    public void deleteUser(Long id){
        this.userRepository.deleteById(id);
    }
}

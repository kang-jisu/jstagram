package com.project.jstagram.controller;

import com.project.jstagram.model.Post;
import com.project.jstagram.model.User;
import com.project.jstagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/user")
public class UserApiController {

    private UserService userService;

    public UserApiController(UserService userService){
        this.userService=userService;
    }


    @GetMapping(produces = "application/json;charset=utf8")
    public List<User> user(Model model){
        List<User> user = userService.findAllUser();
        return user;
    }

    @PostMapping(produces = "application/json; charset=utf8")
    public void createUser(User user) throws IOException {
        userService.createUser(user);
    }

    @DeleteMapping(value = "/{id}",produces = "application/json;charset=utf8")
    public void deleteUser(@PathVariable(value="id") Long id){
        userService.deleteUser(id);
    }

}

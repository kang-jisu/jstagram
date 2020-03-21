package com.project.jstagram.user.controller;

import com.project.jstagram.user.model.User;
import com.project.jstagram.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//일단 구현 안하고 나중에 로그인해서 회원가입 회원정보수정 이런거 하는거로~
@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping
    public String user(Model model){
        List<User> user = userService.findAllUser();
        model.addAttribute("userlist",user);
        return "user";
    }
}

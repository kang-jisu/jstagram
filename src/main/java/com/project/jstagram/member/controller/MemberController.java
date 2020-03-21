package com.project.jstagram.member.controller;

import com.project.jstagram.member.model.Member;
import com.project.jstagram.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/user/signup")
    public String getUserSignUp(){
        return "user/signup";
    }


    @GetMapping("/user/signupResult")
    public String getUserSignUpResult(){
        return "user/signupResult";
    }

    @GetMapping("/user/login")
    public String getUserLogin(){
        return "user/login";
    }

    @GetMapping("/user/login/result")
    public String getUserLoginResult(){
        return "user/loginResult";
    }

    @GetMapping("/user/logout/result")
    public String Logout(){
        return "/";
    }

    @GetMapping("/user/denied")
    public String getUserDenied(Model model){
        model.addAttribute("msg","거부되었습니다.");
        return "user/userDenied";
    }


    @GetMapping("/user/denied/{errormsg}")
    public String getUserDeniedMsg(@PathVariable("errormsg")String msg, Model model){
        model.addAttribute("msg",msg);
        return "user/userDenied";
    }
    // 내 정보 페이지
    @GetMapping("/user/info")
    public String MyInfo(@AuthenticationPrincipal User user, Model model) {
        Optional<Member> m = memberService.findByEmail(user.getUsername());
        model.addAttribute("user",m.get());
        return "user/userInfo";
    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String Admin() {
        return "user/admin";
    }


    @PostMapping(value="/user/signup",produces ="application/json")
    public String postSignUp(  Member member){

        Optional<Member> checkmem= memberService.findByEmail(member.getEmail());
        if(checkmem.isPresent()) return "redirect:/user/denied/emailerror";
        else {
            memberService.signUpMember(member, "y");
            return "redirect:/user/signupResult";
        }
    }

}

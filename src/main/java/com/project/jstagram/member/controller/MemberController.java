package com.project.jstagram.member.controller;

import com.project.jstagram.member.model.Member;
import com.project.jstagram.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/denied")
    public String defaultDeniedPage(Model model) {
        model.addAttribute("msg", "거부되었습니다.");
        return "user/userDenied";
    }

    @GetMapping("/denied/{errormsg}")
    public String DeniedMsgPage(@PathVariable("errormsg") String msg, Model model) {
        model.addAttribute("msg", msg);
        return "user/userDenied";
    }

    // 내 정보 페이지
    @GetMapping("/info")
    public String userInfo(@AuthenticationPrincipal User user, Model model) {
        Optional<Member> m = memberService.findByEmail(user.getUsername());
        model.addAttribute("user", m.get());
        return "user/userInfo";
    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String Admin() {
        return "user/admin";
    }


    // 회원 가입 페이지
    @GetMapping("/signup")
    public String signUpPage() {
        return "user/signup";
    }

    // 회원 가입
    @PostMapping(value = "/signup", produces = "application/json")
    public String signUpMember(Member member) {

        Optional<Member> checkmem = memberService.findByEmail(member.getEmail());
        if (checkmem.isPresent()) return "redirect:/user/denied/emailerror";
        else {
            memberService.signUpMember(member, "y");
            return "redirect:/user/success/signup";
        }
    }
    // 회원 가입 성공
    @GetMapping("/success/signup")
    public String signUpSuccessPage() {
        return "user/signup-success";
    }


    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    // default 로그인 성공 페이지 => default 아닌 경우는 has role 에서 걸려서 로그인한경우라서 원래 보려했던 페이지로 바로 이동함.
    @GetMapping("/success/login")
    public String loginSuccessPage() {
        return "user/login-success";
    }

    @GetMapping("/withdraw")
    public String withdrawMember(@AuthenticationPrincipal User user){
            Optional<Member> m = memberService.findByEmail(user.getUsername());
            if(m.isPresent()) {
                memberService.deleteMember(m.get().getId());
                return "redirect:/user/logout";
            }
            else {
                return "redirect:/user/denied/withdrawerror";
            }
    }

}

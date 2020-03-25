package com.project.jstagram.member.controller;

import com.project.jstagram.member.model.Follow;
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
import java.util.Set;

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

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal User user){
        if(user==null) return "user/login";
        else {
            Optional<Member> m = memberService.findByEmail(user.getUsername());
            if (!m.isPresent()) {
                return "redirect:/user/denied/notfound"; // 임시 에러처리
            } else {
                return "redirect:" + "/" + m.get().getNickname();
            }
        }
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


    // 회원 가입 페이지
    @GetMapping("/update")
    public String updatePage(Model model, @AuthenticationPrincipal User user) {
        Optional<Member> m = memberService.findByEmail(user.getUsername());
        model.addAttribute("user", m.get());
        return "user/update";
    }

    @PostMapping("/update")
    public String updateUserInfo(Member member){
        memberService.updateUserInfo(member);
        return "redirect:/user/info";
    }

    //follow

    @GetMapping("/follow/{id}/login")
    public String loginBeforefollow(@PathVariable("id")Long id){
        Optional<Member> followedMember = memberService.findById(id);

        return "redirect:/"+followedMember.get().getNickname(); //팔로우 눌러서 로그인 했는데 자신일경우 그냥 자기 프로필페이지로 , 아닌경우엔 팔로우
    }


    @GetMapping("/follow/{id}")
    public String followMember(@PathVariable("id")Long id,  @AuthenticationPrincipal User user){
        Optional<Member> loginMember = memberService.findByEmail(user.getUsername());
        Optional<Member> followedMember = memberService.findById(id);

        if(user!=null && loginMember.get()!=followedMember.get()) { // login member -> follow -> followed Member
            memberService.followMember(loginMember.get(),followedMember.get());
        }
        return "redirect:/"+followedMember.get().getNickname(); //팔로우 눌러서 로그인 했는데 자신일경우 그냥 자기 프로필페이지로 , 아닌경우엔 팔로우
    }

}

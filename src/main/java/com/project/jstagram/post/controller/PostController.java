package com.project.jstagram.post.controller;

import com.project.jstagram.member.model.Member;
import com.project.jstagram.member.service.MemberService;
import com.project.jstagram.post.model.Comments;
import com.project.jstagram.post.model.Post;
import com.project.jstagram.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;


    // 메인 페이지
    @GetMapping("/")
    public String indexPageGetPostList(Model model, Principal principal) {
        List<Post> post = postService.findAllPost();
        List<Member> member = memberService.findAllMember();
        Map<Long, String> map = new HashMap<>();
        Map<Long, List<Comments>> Cmap = new HashMap<>();
        for (Member m : member) {
            map.put(m.getId(), m.getMemberId());
        }
        for (Post p : post) {
            Cmap.put(p.getId(), postService.findComments(p.getId()));
        }
        model.addAttribute("comments", Cmap); //글마다의 댓글목록
        model.addAttribute("postList", post); //글 목록
        model.addAttribute("mapList", map); //post,comments의 author(=member_id) 로 memberId 닉네임검색용

        if (principal != null) {
            Optional<Member> m = memberService.findByEmail(principal.getName());
            model.addAttribute("user", m.get()); // 현재 로그인한 유저와 post,comment author 비교해서 crud 권한 제공
        }
        return "index";
    }

    @GetMapping("/insert") // 등록 페이지
    public String insert() {
        return "insert";
    }


    @PostMapping("/upload")
    public String uploadPost(@RequestParam(value = "file") MultipartFile file,
                             Post post, Principal principal) throws IOException {

        Optional<Member> m = memberService.findByEmail(principal.getName());
        post.setAuthor(m.get().getId());
        postService.uploadFile(file, post);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable(value = "id") Long id) {
        postService.deleteOne(id);
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detailPost(@PathVariable(value = "id") Long id, Model model, @AuthenticationPrincipal User user) {
        //글 디테일
        Post post = postService.getOne(id);
        String author = postService.findAuthorByid(post.getAuthor());
        model.addAttribute("post", post);
        model.addAttribute("author", author);

        ///유저이름
        List<Member> member = memberService.findAllMember();
        Map<Long, String> map = new HashMap<>();
        for (Member m : member) {
            map.put(m.getId(), m.getMemberId());
        }
        model.addAttribute("mapList", map); //댓글, 글 author->user닉네임검색용

        //댓글
        Map<Long, List<Comments>> Cmap = new HashMap<>();
        Cmap.put(post.getId(), postService.findComments(post.getId()));
        model.addAttribute("comments", Cmap); //글마다의 댓글목록

        if (user != null) {
            Optional<Member> m = memberService.findByEmail(user.getUsername());
            model.addAttribute("user", m.get());
        }

        return "detail";
    }

    // 수정 페이지
    @GetMapping("/update/{id}")
    public String updatePost(@PathVariable(value = "id") Long id, Model model) {
        Post post = postService.getOne(id);
        model.addAttribute("post", post);
        return "update";
    }

    // 글 수정
    @PostMapping("/update/{id}")
    public String updatePost(@RequestParam(value = "file") MultipartFile file, Post post, @PathVariable(value = "id") Long id) throws IOException {
        //지금 board는 update에서 받아온 값.
        //service에 id넘겨줘서 id에있는값(기존값)을 받아온값(post)로 변경
        postService.updatePost(file, id, post);
        return "redirect:/";
    }

}
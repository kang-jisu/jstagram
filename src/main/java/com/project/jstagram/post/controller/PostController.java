package com.project.jstagram.post.controller;

import com.project.jstagram.member.model.Member;
import com.project.jstagram.member.service.MemberService;
import com.project.jstagram.post.model.Comments;
import com.project.jstagram.post.model.Post;
import com.project.jstagram.post.service.CommentsService;
import com.project.jstagram.post.service.PostService;
import com.project.jstagram.post.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private S3Service s3Service;


    @GetMapping("/header")
    public String header() {
        return "/layout/header";
    }

    @GetMapping
    public String post(Model model, Principal principal) {
        List<Post> post = postService.findAllPost();
//        List<User> user = userService.findAllUser();
        List<Member> member = memberService.findAllMember();
        Map<Long, String> map = new HashMap<>();
        Map<Long, List<Comments>> Cmap = new HashMap<>();
        for (Member m : member) {
            map.put(m.getId(), m.getMemberId()); //작성자 이름알아오기
        }
        for (Post p : post) {
            Cmap.put(p.getId(), postService.findComments(p.getId()));
        }
        model.addAttribute("comments", Cmap); //글마다의 댓글목록
        model.addAttribute("postList", post); //글 목록
        model.addAttribute("mapList", map); //글쓴이 author->user닉네임검색용

        if (principal != null) {
            System.out.println(principal);
            Optional<Member> m = memberService.findByEmail(principal.getName());
            model.addAttribute("user", m.get());

        }
        return "index";
    }

    @GetMapping("/insert") //등록
    public String insert() {
        return "insert";
    }


    @PostMapping("/upload")
    public String uploadPost(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file,
                             Post post, Principal principal) throws IOException {

//        s3Service.upload(file,"jstagram");
        Optional<Member> m = memberService.findByEmail(principal.getName());
        post.setAuthor(m.get().getId());
        postService.uploadFile(file, post);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable(value = "id") Long id) {
        commentsService.deleteAllByPostId(id);
        postService.deleteOne(id);
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detailPost(@PathVariable(value = "id") Long id, Model model, Post post, @AuthenticationPrincipal User user) {
        //글 디테일
        post = postService.getOne(id);
        String author = postService.findAuthorByid(post.getAuthor());
        model.addAttribute("post", post);
        model.addAttribute("author", author);

        ///유저이름
        List<Member> member = memberService.findAllMember();
        Map<Long, String> map = new HashMap<>();
        for (Member m : member) {
            map.put(m.getId(), m.getMemberId()); //작성자 이름알아오기
        }
        model.addAttribute("mapList", map); //글쓴이 author->user닉네임검색용

        //댓글
        Map<Long, List<Comments>> Cmap = new HashMap<>();
        Cmap.put(post.getId(), postService.findComments(post.getId()));
        model.addAttribute("comments", Cmap); //글마다의 댓글목록

        if (user != null) {
            System.out.println(user.getUsername());
            System.out.println(user.getAuthorities());
            Optional<Member> m = memberService.findByEmail(user.getUsername());
            model.addAttribute("user", m.get());

        }

        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updatePost(@PathVariable(value = "id") Long id, Model model, Post post) {
        post = postService.getOne(id);
        model.addAttribute("post", post);
        return "update";
    }

    @PostMapping("/update/{id}")
    public String updatePost(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file, Post post, @PathVariable(value = "id") Long id, Principal principal) throws IOException {
        //지금 board는 update에서 받아온 값.
        //service에 id넘겨줘서 id에있는값(기존값)을 받아온값(post)로 변경
        Optional<Member> m = memberService.findByEmail(principal.getName());
        post.setAuthor(m.get().getId());
        postService.updatePost(file, id, post);
        return "redirect:/";
    }

}
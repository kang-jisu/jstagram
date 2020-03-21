package com.project.jstagram.post.controller;

import com.project.jstagram.post.model.Comments;
import com.project.jstagram.post.model.Post;
import com.project.jstagram.user.model.User;
import com.project.jstagram.post.service.CommentsService;
import com.project.jstagram.post.service.PostService;
import com.project.jstagram.post.service.S3Service;
import com.project.jstagram.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class PostController {

    private PostService postService;

    public PostController(PostService postService){
        this.postService=postService;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private CommentsService commentsService;

    @Autowired
    private S3Service s3Service;


    @GetMapping("/header")
    public String header(){
        return "/layout/header";
    }
    @GetMapping
    public String post(Model model){
        List<Post> post = postService.findAllPost();
        List<User> user = userService.findAllUser();
        Map<Long,String> map = new HashMap<>();
        Map<Long,List<Comments>> Cmap = new HashMap<>();
        for (User u : user){
            map.put(u.getId(),u.getUserId()); //작성자 이름알아오기
        }
        for (Post p :post) {
            Cmap.put(p.getId(),postService.findComments(p.getId()));
        }
        model.addAttribute("comments",Cmap); //글마다의 댓글목록
        model.addAttribute("postList",post); //글 목록
        model.addAttribute("mapList",map); //글쓴이 author->user닉네임검색용
      return "index";
    }

    @GetMapping("/insert") //등록
    public String insert(){
        return "insert";
    }

    @PostMapping("/insert")
    public String createPost(  Post post, Model model){
        postService.createPost(post);
        List<Post> postList = postService.findAllPost();
        model.addAttribute("postList",postList);
        return "redirect:/";
    }

    @PostMapping("/upload")
    public String uploadPost(HttpServletRequest request, @RequestParam(value="file") MultipartFile file,
                              Post post) throws IOException {

//        s3Service.upload(file,"jstagram");
        postService.uploadFile(file,post);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable(value="id") Long id){
        commentsService.deleteAllByPostId(id);
        postService.deleteOne(id);
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detailPost(@PathVariable(value="id")Long id, Model model,Post post){
        //글 디테일
        post = postService.getOne(id);
        String author = postService.findAuthorByid(post.getAuthor());
        model.addAttribute("post",post);
        model.addAttribute("author",author);

        ///유저이름
        List<User> user = userService.findAllUser();
        Map<Long,String> map = new HashMap<>();
        for (User u : user){
            map.put(u.getId(),u.getUserId()); //작성자 이름알아오기
        }
        model.addAttribute("mapList",map); //글쓴이 author->user닉네임검색용

        //댓글
        Map<Long,List<Comments>> Cmap = new HashMap<>();
        Cmap.put(post.getId(),postService.findComments(post.getId()));
        model.addAttribute("comments",Cmap); //글마다의 댓글목록


        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updatePost(@PathVariable(value="id")Long id, Model model,Post post){
        post = postService.getOne(id);
        model.addAttribute("post",post);
        return "update";
    }

    @PostMapping("/update/{id}")
    public String updatePost(HttpServletRequest request, @RequestParam(value="file") MultipartFile file,Post post,@PathVariable(value="id")Long id) throws IOException{
        //지금 board는 update에서 받아온 값.
        //service에 id넘겨줘서 id에있는값(기존값)을 받아온값(post)로 변경
        postService.updatePost(file,id,post);
        return "redirect:/";
    }

}

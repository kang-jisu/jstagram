package com.project.jstagram.post.controller;

import com.project.jstagram.post.model.Post;
import com.project.jstagram.post.service.CommentsService;
import com.project.jstagram.post.service.PostService;
import com.project.jstagram.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@RequestMapping("/api/")
@RestController
@CrossOrigin(origins="*")
public class PostApiController {

    private PostService postService;

    public PostApiController(PostService postService){
        this.postService=postService;
    }

    @Autowired
    private MemberService memberService;
    @Autowired
    private CommentsService commentsService;

    //Post List Api
    @GetMapping(value="/posts",produces = "application/json; charset=utf8")
    public List<Post> getPostList(){
        List<Post> posts = postService.findAllPost();
        return posts;
    }


    //Detail Post Api
    @GetMapping(value="/post/{postid}",produces = "application/json; charset=utf8")
    public Post getPostById(@PathVariable(value="postid")Long id){
        //글 디테일
        Post post = postService.getOne(id);
        return post;
    }

    @PostMapping(value="/post",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadPost(HttpServletRequest request, @RequestParam(value="file") MultipartFile file,
                           Post post ) throws IOException {
        System.out.println("------------------------------------------------------");
        System.out.println(file);
        postService.upload(file,post);
        System.out.println("------------------------------------------------------");
    }

    @DeleteMapping(value = "/post/{id}",produces = "application/json;charset=utf8")
    public void deletePost(@PathVariable(value="id") Long id){
        commentsService.deleteAllByPostId(id);
        postService.deleteOne(id);
    }

    @PutMapping("/post/{id}")
    public void updatePost(HttpServletRequest request, @RequestParam(value="file") MultipartFile file,Post post,@PathVariable(value="id")Long id) throws IOException{
//        //지금 board는 update에서 받아온 값.
//        //service에 id넘겨줘서 id에있는값(기존값)을 받아온값(post)로 변경
        postService.updatePost(file,id,post);
//        return "redirect:/";
    }

}

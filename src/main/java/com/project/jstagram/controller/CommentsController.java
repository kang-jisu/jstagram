package com.project.jstagram.controller;


import com.project.jstagram.model.Comments;
import com.project.jstagram.model.Post;
import com.project.jstagram.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;

@Controller
@RequestMapping("")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping("/comments")
    public String createComment( Comments comments){
        commentsService.createComment(comments);
        return "redirect:/";
    }

    @GetMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id){
        commentsService.deleteOne(id);
        return "redirect:/";
    }


    //deatil페이지 댓글작성
    @PostMapping("/detail/comments/{id}")
    public String createCommentDetail( @PathVariable("id") Long id,Comments comments){
        commentsService.createComment(comments);
        return "redirect:/detail/"+id;
    }


    //detail페이지 댓글 삭제하고 원래 보던 그 detail페이지로 가게
    @GetMapping("/detail/{postid}/comments/delete/{id}")
    public String deleteCommentDetail(@PathVariable("postid") Long postid, @PathVariable("id") Long id){
        commentsService.deleteOne(id);
        return "redirect:/detail/"+postid;
    }
}

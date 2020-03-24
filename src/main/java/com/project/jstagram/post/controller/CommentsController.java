package com.project.jstagram.post.controller;
import com.project.jstagram.post.model.Comments;
import com.project.jstagram.post.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping("")
    public String createComment(Comments comments) {
        commentsService.createComment(comments);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        commentsService.deleteOne(id);
        return "redirect:/";
    }


    //deatil페이지 댓글작성
    @PostMapping("/detail/{postid}")
    public String createCommentDetail(@PathVariable("postid") Long postid, Comments comments) {
        commentsService.createComment(comments);
        return "redirect:/detail/" + postid;
    }


    //detail페이지 댓글 삭제하고 원래 보던 그 detail페이지로 가게
    @GetMapping("/detail/{postid}/delete/{id}")
    public String deleteCommentDetail(@PathVariable("postid") Long postid, @PathVariable("id") Long id) {
        commentsService.deleteOne(id);
        return "redirect:/detail/" + postid;
    }
}
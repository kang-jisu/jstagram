package com.project.jstagram.controller;

import com.project.jstagram.model.Comments;
import com.project.jstagram.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/")
@RestController
@CrossOrigin(origins="*")
public class CommentsApiController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping(value="/comments",produces = "application/json; charset=utf8")
    public Long createComment( Comments comments){
        Long id = comments.getPostId();
        commentsService.createComment(comments);
        return id;
    }

    @DeleteMapping(value="/comments/{id}",produces = "application/json; charset=utf8")
    public Long deleteComment(@PathVariable("id") Long id){
        Long pid = commentsService.getPostIdByCommentId(id);
        commentsService.deleteOne(id);
        return pid;
    }

}

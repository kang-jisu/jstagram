package com.project.jstagram.service;

import com.project.jstagram.model.Comments;
import com.project.jstagram.model.Post;
import com.project.jstagram.repository.CommentsRepository;
import com.project.jstagram.repository.PostRepository;
import com.project.jstagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class CommentsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    public void createComment(Comments comments){
        comments.setRegDate(new Date());
        this.commentsRepository.save(comments);
    }

    @Transactional
    public void deleteOne(Long id){
        commentsRepository.deleteById(id);
    }


    public void deleteAllByPostId(Long id) {
        Post post = postRepository.getOne(id);
        commentsRepository.deleteAll(post.getComments());
    }

    public Long getPostIdByCommentId(Long id){
        Comments c = commentsRepository.getOne(id);
        return c.getPostId();
    }
}

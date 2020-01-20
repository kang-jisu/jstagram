package com.project.jstagram.service;


import com.project.jstagram.model.Comments;
import com.project.jstagram.model.Post;
import com.project.jstagram.model.User;
import com.project.jstagram.repository.PostRepository;
import com.project.jstagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private S3Service s3Service;


    public PostService(PostRepository postRepository){
        this.postRepository=postRepository;
    }


    public Post getOne(Long id){
        return this.postRepository.getOne(id);
    }


    public String findAuthorByid(Long id){
        User user = userRepository.getOne(id);
        return user.getUserId();
    }

    public List<Comments> findComments(Long id){
        Post post = postRepository.getOne(id);
        return post.getComments();
    }

    public List<Post> findAllPost(){
        return postRepository.findAll();
    }

    public void createPost(Post post){
        post.setRegDate(new Date());
        this.postRepository.save(post);
    }

    @Transactional
    public void deleteOne(Long id){
        String filename=postRepository.getOne(id).getImage();
        int idx = filename.lastIndexOf("/");
        s3Service.delete(filename.substring(idx+1));
        postRepository.deleteById(id);

    }

    @Transactional
    public void updatePost(MultipartFile file,Long id,Post newpost) throws IOException{
        Post oldpost = postRepository.getOne(id);
        oldpost.setRegDate(new Date());
        oldpost.setContent(newpost.getContent());

        if(file.isEmpty())oldpost.setImage(oldpost.getImage()); //file 선택된게 없이 수정할땐 기존 파일 그대로
        else {
            String oldfilename=postRepository.getOne(id).getImage();
            int idx = oldfilename.lastIndexOf("/");
            s3Service.delete(oldfilename.substring(idx+1));
            String storedFileName = s3Service.upload(file,"jstagram");
            oldpost.setImage(storedFileName); //디비에 파일 저장
        }
        this.postRepository.save(oldpost);}


    @Transactional
    public Post uploadFile(MultipartFile file,Post post) throws IOException {
        post.setRegDate(new Date());

        if(!file.isEmpty()) {
            String storedFileName = s3Service.upload(file,"jstagram");
            post.setImage(storedFileName);
        }

       return  this.postRepository.save(post);
    }

    //api구현용
    public void upload(MultipartFile file,Post post) throws IOException{
        post.setRegDate(new Date());

        if(!file.isEmpty()) {

            String storedFileName = s3Service.upload(file,"jstagram");
            post.setImage(storedFileName);

        }
        this.postRepository.save(post);
    }
}

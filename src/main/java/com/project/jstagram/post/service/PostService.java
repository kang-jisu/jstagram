package com.project.jstagram.post.service;


import com.project.jstagram.member.model.Member;
import com.project.jstagram.member.repository.MemberRepository;
import com.project.jstagram.post.model.Comments;
import com.project.jstagram.post.model.Post;
import com.project.jstagram.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private S3Service s3Service;

    // api 용 # TODO findById로 해서 Optional error 걸어주기
    public Post getOne(Long id){
        return this.postRepository.getOne(id);
    }

    public String findAuthorByid(Long id){
        Member member= memberRepository.getOne(id);
        return member.getNickname();
    }

    public List<Comments> findComments(Long id){
        Post post = postRepository.getOne(id);
        return post.getComments();
    }

    public List<Post> findAllPost(){
        return postRepository.findAll();
    }


    public List<Post> findAllPostByAuthor(Long id){
        return postRepository.findByAuthor(id);
    }

    @Transactional
    public void deleteOne(Long id){
        commentsService.deleteAllByPostId(id); //post에 딸린 comments 먼저 지우
        String filename=postRepository.getOne(id).getImage();
        if(filename!=null) {
            int idx = filename.lastIndexOf("/");
            s3Service.delete(filename.substring(idx + 1));
        } //S3에서 파일 지우기
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
            if(oldfilename!=null) {
                int idx = oldfilename.lastIndexOf("/");
                s3Service.delete(oldfilename.substring(idx + 1));
            }
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
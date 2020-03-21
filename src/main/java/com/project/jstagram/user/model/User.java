package com.project.jstagram.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.jstagram.post.model.Comments;
import com.project.jstagram.post.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name="user")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String userId;

    @Column
    private String name;

    @Column
    private String text;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String sex;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL )
    @JoinColumn(name="author")
    private List<Post> post;

    @OneToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="author")
    private List<Comments> comments;

    public User(){
    }

    @Builder
    public User(Long id,String userId, String name, String text, String email, String phone, String sex,List<Post> post,List<Comments> comments) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.text = text;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.post=post;
        this.comments=comments;
    }
}

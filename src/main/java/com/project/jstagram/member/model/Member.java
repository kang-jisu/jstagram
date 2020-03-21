package com.project.jstagram.member.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.jstagram.post.model.Comments;
import com.project.jstagram.post.model.Post;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Setter
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"}) // 이 어노테이션 무슨의미인지 알아보기 2019.11.22
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=30,nullable=false)
    private String email;

    @Column(length=100,nullable = false)
    private String password;

    @Column
    private String memberId;

    @Column
    private String text;

    @Column
    private String phone;

    @Column
    private String birth;

    @Column
    private LocalDateTime createdDate;
    @Column
    private LocalDateTime modifiedDate;

    @Column
    private String verify;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL )
    @JoinColumn(name="author")
    private List<Post> post;

    @OneToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="author")
    private List<Comments> comments;

    public Member(){

    }
    @Builder
    public Member(String email, String password, String memberId, String text, String phone, String birth, LocalDateTime createdDate, LocalDateTime modifiedDate, String verify, List<Post> post, List<Comments> comments) {
        this.email = email;
        this.password = password;
        this.memberId = memberId;
        this.text = text;
        this.phone = phone;
        this.birth = birth;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.verify = verify;
        this.post = post;
        this.comments = comments;
    }
}
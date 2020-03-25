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
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=30,nullable=false)
    private String email; // 로그인 이메일

    @Column(length=100,nullable = false)
    private String password; // 로그인 패스워드

    @Column
    private String memberId; // 계정 아이디

    @Column
    private String name; // 이름

    @Column
    private String text; // 프로필 소개

    @Column
    private String phone; // 번호

    @Column
    private String birth; //생년월일

    @Column
    private LocalDateTime createdDate; // 계정 생성일
    @Column
    private LocalDateTime modifiedDate; // 정보 변경일

    @Column
    private String verify; // 회원가입 후 이메일인증같은 권한 확인-> 지금은 모두 y(yes) # TODO 나중엔 변수 넣고 이메일같은거 인증 넣어서 링크확인해야 y로 변경

    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL )
    @JoinColumn(name="author")
    private List<Post> post;

    @OneToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="author")
    private List<Comments> comments;

    public Member(){

    }
    @Builder
    public Member(String email, String password, String memberId, String name, String text, String phone, String birth, LocalDateTime createdDate, LocalDateTime modifiedDate, String verify, List<Post> post, List<Comments> comments) {
        this.email = email;
        this.password = password;
        this.memberId = memberId;
        this.name = name;
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
package com.project.jstagram.post.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name="post")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String content;

    @Column
    private String image;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regDate;

    @Column
    private Long author;

    @OneToMany(fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="postId")
    private List<Comments> comments;


    public Post(){
    }

    @Builder
    public Post(Long id, String content, String image, Date regDate, Long author,List<Comments> comments) {
        this.id = id;
        this.content = content;
        this.image = image;
        this.regDate = regDate;
        this.author=author;
        this.comments=comments;
    }
}

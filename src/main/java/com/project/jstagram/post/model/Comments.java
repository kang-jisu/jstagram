package com.project.jstagram.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name="comments")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long postId;

    @Column
    private Long author;

    @Column
    private String comment;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regDate;

    public Comments(){
    }

    public Comments(Long id,Long postId, Long author, String comment, Date regDate) {
        this.id = id;
        this.postId = postId;
        this.author = author;
        this.comment = comment;
        this.regDate = regDate;
    }
}

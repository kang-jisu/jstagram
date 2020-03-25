package com.project.jstagram.member.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity
@Setter
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name="follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long followerId; //당한사람

    @Column
    private Long followingId; //한사람

    public Follow(){

    }
    @Builder
    public Follow(Long id, Long followerId, Long followingId){
        this.id=id;
        this.followingId=followerId;
        this.followingId=followingId;
    }

}

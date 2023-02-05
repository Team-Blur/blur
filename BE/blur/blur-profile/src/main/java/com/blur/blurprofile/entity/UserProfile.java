package com.blur.blurprofile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_profile")
public class UserProfile {

    @JsonIgnore
    @Column(name = "user_no")
    @Id
    private Long userNo;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "age")
    private Integer age;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "image")
    private String image;

    @Column(name = "gender")
    private String gender;

    @Column(name = "introduce")
    private String introduce;

    public void updateProfile(Integer age, String nickname, String image, String gender, String introduce) {
        this.age = age;
        this.nickname = nickname;
        this.image = image;
        this.gender = gender;
        this.introduce = introduce;
    }

}

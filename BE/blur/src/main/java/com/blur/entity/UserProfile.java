package com.blur.entity;

import javax.persistence.Column;

public class UserProfile {

    @Column(name = "age")
    Integer age;
    @Column(name = "gender")
    Boolean gender;

    @Column(name = "nickname")
    String nickname;
}

package com.blur.api.dto.request;

import com.blur.entity.User;
import com.blur.entity.UserProfile;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class UserInfoDto {

    private Long userNo;

    private String userId;

    private String password;

    private String email;

    private Boolean gender;

    private Integer birthyear;

    private String nickname;

    private String image;

    public void encryptPassword(String BCryptpassword) {
        this.password = BCryptpassword;
    }

    /* DTO -> Entity */
    public User toEntity() {
        User user = User.builder()
                .userId(userId)
                .password(password)
                .email(email)
                .build();
        return user;
    }

}

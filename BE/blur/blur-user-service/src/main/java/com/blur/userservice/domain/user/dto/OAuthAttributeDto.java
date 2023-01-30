package com.blur.userservice.domain.user.dto;

import java.util.Map;

import com.blur.userservice.domain.user.entity.AuthType;
import com.blur.userservice.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAttributeDto {
    private Map<String, Object> attributes; // OAuth2 반환하는 유저정보 MAP
    private String nameAttributeKey;
//    private String name;
    private String userId;
    private String email;
    private AuthType authType;


    public OAuthAttributeDto(Map<String, Object> attributes, String nameAttributeKey, String userId, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.userId = userId;
        this.email = email;
    }

    public static OAuthAttributeDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        // 여기서 네이버와 카카오 등 구분 (ofNaver, ofKakao)
        if("naver".equals(registrationId))
            return ofNaver(userNameAttributeName , attributes);
        if("kakao".equals(registrationId))
            return ofKakao(userNameAttributeName , attributes);
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributeDto ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributeDto.builder()
//                .name((String) response.get("name"))
        		.userId((String) response.get("id"))
                .email((String) response.get("email"))
                .nameAttributeKey("id")
                .attributes(response)
                .authType(AuthType.NAVER)
                .build();
    }


    private static OAuthAttributeDto ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributeDto.builder()
//                .name((String) attributes.get("name"))
        		.userId((String) attributes.get("sub"))
                .email((String) attributes.get("email"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .authType(AuthType.GOOGLE)
                .build();
    }
    
    private static OAuthAttributeDto ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//			Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
			
			return OAuthAttributeDto.builder()
//			.name((String) kakaoProfile.get("nickname"))
			.userId((String) kakaoAccount.get("sub"))
			.email((String) kakaoAccount.get("email"))
			.attributes(kakaoAccount)
			.authType(AuthType.KAKAO)
			.build();
    }
  
//    public Customer toEntity(OAuthAttributeDto attributeDto){
//        return new Customer(email,"temp",name,null, attributeDto.getAuthType());
//    }

    public User toEntity(OAuthAttributeDto attributeDto) {
    	return new User(userId, email, "null", attributeDto.getAuthType());
    }
}
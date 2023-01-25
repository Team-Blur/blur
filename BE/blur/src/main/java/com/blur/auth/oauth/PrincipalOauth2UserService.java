package com.blur.auth.oauth;

import com.blur.auth.PrincipalDetails;
import com.blur.auth.oauth.provider.GoogleUserInfo;
import com.blur.auth.oauth.provider.KakaoUserInfo;
import com.blur.auth.oauth.provider.NaverUserInfo;
import com.blur.auth.oauth.provider.OAuth2UserInfo;
import com.blur.entity.User;
import com.blur.entity.UserProfile;
import com.blur.repository.UserProfileRepository;
import com.blur.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    //구글로부터 받은 userRequest데이터에 대해 후처리되는함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }else {
            System.out.println("error");
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String userId = provider+"_"+providerId;
        String email = oAuth2UserInfo.getEmail();

        User userEntity = userRepository.findByUserId(userId);

        if(userEntity==null) {
            userEntity = User.builder()
                    .userId(userId)
                    .email(email)
                    .build();
            userRepository.save(userEntity);

            UserProfile userProfile = new UserProfile();
            userProfile.setUser(userEntity);
            System.out.println("user.getUserNo()```````````````````````````````````````````````");
            System.out.println(userEntity.getUserNo());
            System.out.println(userEntity);
            System.out.println(userProfile.getUser());
            System.out.println(userProfile.getUserNo());
            System.out.println("```````````````````````````````````````````````");
            userProfileRepository.save(userProfile);
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}

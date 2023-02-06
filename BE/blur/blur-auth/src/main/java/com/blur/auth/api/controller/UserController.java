package com.blur.auth.api.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blur.auth.api.dto.ErrorResponse;
import com.blur.auth.api.entity.User;
import com.blur.auth.api.entity.UserDto;
import com.blur.auth.api.entity.UserRefreshToken;
import com.blur.auth.api.repository.UserRefreshTokenRepository;
import com.blur.auth.api.repository.UserRepository;
import com.blur.auth.api.service.EmailService;
import com.blur.auth.api.service.PasswordService;
import com.blur.auth.api.service.UserService;
import com.blur.auth.common.ApiResponse;
import com.blur.auth.common.ApiResponseHeader;
import com.blur.auth.config.properties.AppProperties;
import com.blur.auth.oauth.entity.AuthToken;
import com.blur.auth.oauth.entity.AuthTokenProvider;
import com.blur.auth.oauth.entity.UserPrincipal;
import com.blur.auth.utils.CookieUtil;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	
    private final UserService userService;
    private final UserRepository userRepository;
	private EmailService emailService;
	private PasswordService passwordService;
	private final MessageSource messageSource;
	private final PasswordEncoder passwordEncoder;
	private final AuthTokenProvider tokenProvider;
	private final AppProperties appProperties;
	private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
	
	private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @GetMapping
    public ApiResponse getUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ApiResponse.success("user", user);
    }
    
    
    /**
     * 로그인 JWT 발급
     * @param userInfo {email, password}
     * @return
     */
    @PostMapping("/login")
    public ApiResponse login(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String, String> userInfo) {
        User user = userRepository.findByUserId(userInfo.get("userId"));
        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ErrorResponse(messageSource.getMessage("error.none.user", null, LocaleContextHolder.getLocale())));
            return ApiResponse.fail();
        }

        if (!passwordEncoder.matches(userInfo.get("password"), user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ErrorResponse(messageSource.getMessage("error.wrong.password", null, LocaleContextHolder.getLocale())));
        	return ApiResponse.fail();
        }

//        String token = jwtTokenProvider.createToken(user.getUsername(), user.getUserSeq());
//        Date now = new Date();
//        AuthToken accessToken = tokenProvider.createAuthToken(
//        		user.getUserId(),
//                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
//        );
//
//        // refresh 토큰 설정
//        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
//
//        AuthToken refreshToken = tokenProvider.createAuthToken(
//                appProperties.getAuth().getTokenSecret(),
//                new Date(now.getTime() + refreshTokenExpiry)
//        );
//
//        return ResponseEntity.ok(new LoginUserResponse(accessToken, refreshToken));
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		userInfo.get("userId"),
                		userInfo.get("password")
                )
        );
        
        String userId = userInfo.get("userId");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                userId,
                ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
        if (userRefreshToken == null) {
            // 없는 경우 새로 등록
            userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        } else {
            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return ApiResponse.success("token", accessToken.getToken());
    }
    
    
    @PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDto userDto) throws Exception{
    	System.out.println(userDto);
		userService.register(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	@PostMapping("/checkId") // 아이디 중복체크
	public ResponseEntity<Boolean> checkId(@RequestBody Map<String,String> param) {

		String userId = param.get("userId");
		Boolean res = userService.checkId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(res);

	}

	@PostMapping("/sendAuthEmail") // 이메일 인증메일 발송
	public ResponseEntity<?> sendAuthEmail(@RequestBody Map<String, String> param) throws Exception {

		String email = param.get("email");
		System.out.println("controller : " + email);
		emailService.sendAuthMessage(email);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PutMapping("/findPassword") // 비밀번호 찾기
	public ResponseEntity<Boolean> findPassword(@RequestBody Map<String, String> param) throws Exception {

		String userId = param.get("userId");
		Boolean res = passwordService.sendTempPassword(userId);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
	
	
	
	@Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class LoginUserResponse {
        private AuthToken accessToken;
        private AuthToken refreshToken;
        public LoginUserResponse(AuthToken accessToken, AuthToken refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}

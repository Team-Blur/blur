package com.blur.blurprofile.controller;

import com.blur.blurprofile.dto.InterestDto;
import com.blur.blurprofile.dto.ProfileDto;
import com.blur.blurprofile.dto.ResponseCard;
import com.blur.blurprofile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile/{id}")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileDto> getProfile(@PathVariable("id") String userId) {
        //매칭정보도 가져와야됨
        ProfileDto profileDto = profileService.getProfile(userId);
        return ResponseEntity.status(HttpStatus.OK).body(profileDto);
    }

    @PutMapping("/updateProfile") //프로필 변경
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDto profileDto) throws Exception {
        ProfileDto profile= profileService.updateProfile(profileDto);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @GetMapping("getAllInterests")
    public ResponseEntity<InterestDto> getAllInterests(@PathVariable("id") String userId) {
        InterestDto interestDto = profileService.getAllInterests(userId);
        return ResponseEntity.status(HttpStatus.OK).body(interestDto);
    }

    @GetMapping("/getCard")
    public ResponseEntity<ResponseCard> getCard(@PathVariable("id") String userId) {

        ResponseCard responseCard = profileService.getCard(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseCard);
    }

    @PutMapping("/updateInterest")
    public ResponseEntity<?> updateInterest(@RequestBody ProfileDto.RequestUserInterestDto requestUserInterestDto, @PathVariable("id") String userId) throws Exception {
        profileService.updateInterest(requestUserInterestDto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}

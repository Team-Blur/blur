package com.blur.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "token")
public class Token {
	 @JsonIgnore
	    @Id
	    @Column(name = "user_no")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    Long userNo;
	 
	 @Column(name = "refresh_token", length = 255)
	    String refreshToken;
}

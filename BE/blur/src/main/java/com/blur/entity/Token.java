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
//	@JsonIgnore
//	@Id
//	@Column(name = "Member_no")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	Long MemberNo;
	
	@Id
    @Column(name = "member_id", nullable = false, length = 255, unique = true)
    private String memberId;

	@Column(name = "refresh_token", length = 255)
	private String refreshToken;
	

    public static Token createToken(String memberId, String refreshToken){
        return new Token(memberId, refreshToken);
    }

    public void changeToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

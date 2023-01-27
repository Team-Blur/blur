package com.blur.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blur.entity.Token;

public interface TokenRepository extends JpaRepository<Token, String>{
	Optional<Token> findByMemberId(String memberId);
	void deleteByMemberId(String memberId);
}

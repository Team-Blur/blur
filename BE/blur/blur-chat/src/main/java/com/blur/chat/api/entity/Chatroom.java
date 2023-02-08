package com.blur.chat.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blur.chat.api.dto.FeignUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "chatroom")
public class Chatroom {

    @Id
	private Long chatroomNo;
	@Column
	private Long userNo;
	
	public Chatroom(Long userNo) {
		super();
		this.userNo = userNo;
	}
}

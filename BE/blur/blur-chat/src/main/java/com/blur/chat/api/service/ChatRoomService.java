package com.blur.chat.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blur.chat.api.dto.ResponseDto;
import com.blur.chat.api.dto.UserInfoDto;
import com.blur.chat.api.entity.Chatroom;
import com.blur.chat.api.repository.ChatRoomNoRepository;
import com.blur.chat.api.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

	public final ChatRoomRepository chatRoomRepository;
	public final ChatRoomNoRepository chatRoomNoRepository;

	public void enterChatRoom(String roomNo, String sessionId, String username) {
		chatRoomRepository.enterChatRoom(roomNo, sessionId, username);
	}

	public String disconnectWebsocket(String sessionId) {
		return chatRoomRepository.disconnectWebsocket(sessionId);
	}

	public String leaveChatRoom(String sessionId) {
		return chatRoomRepository.leaveChatRoom(sessionId);
	}

    public List<String> findUser(String roomNo,String sessionId){
       return chatRoomRepository.findUsersInChatroom(roomNo,sessionId);
    }
    
    public Long createChatroom(UserInfoDto userInfoDto){
    	Chatroom chatroom = new Chatroom(userInfoDto.getUserNo(), userInfoDto.getNickname());
    	System.out.println(chatroom.toString());
    	chatRoomNoRepository.save(chatroom);
    	Long chatroomNo = chatroom.getChatroomNo();
    	System.out.println("createChatroom : " + chatroomNo);
    	
    	return chatroomNo;
    }
    
    public Chatroom enterChatroom(UserInfoDto userInfoDto, Long chatroomNo){
//    	Chatroom chatroom = new Chatroom();
//    	chatroom.enterWoman(userInfoDto, chatroomNo);
//    	System.out.println(chatroom.toString());
    	
//    	System.out.println("createChatroom : " + chatroomNo.toString());
    	Chatroom chatroom = chatRoomNoRepository.findByChatroomNo(chatroomNo);
    	chatroom.update(userInfoDto.getUserNo(), userInfoDto.getUserId());
    	
    	return chatroom;
    }
    
    public List<Chatroom> getWomanRooms(Long womanNo){
    	List<Chatroom> result = chatRoomNoRepository.findByWomanNo(womanNo);
    	return result;
    }
    
    public List<Chatroom> getManRooms(Long manNo){
    	List<Chatroom> result = chatRoomNoRepository.findByManNo(manNo);
    	return result;
    }
}

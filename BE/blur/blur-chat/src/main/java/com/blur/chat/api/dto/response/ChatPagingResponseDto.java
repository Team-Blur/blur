package com.blur.chat.api.dto.response;

import com.blur.chat.api.dto.request.ChatMessageSaveDto;
import com.blur.chat.api.entity.Chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Builder
@Setter
@AllArgsConstructor
public class ChatPagingResponseDto {

    private Long chatroomNo;
    private String writer;
    private String message;
    private String createdAt;
    private String nickname;

    public static ChatPagingResponseDto of(Chat chat){
        return ChatPagingResponseDto.builder()
                .writer(chat.getUsers())
                .chatroomNo(chat.getChatroom().getChatroomNo())
                .createdAt(chat.getCreatedAt())
                .message(chat.getMessage())
                .build();
    }

    public static ChatPagingResponseDto byChatMessageDto(ChatMessageSaveDto chatMessageSaveDto){
        return ChatPagingResponseDto.builder()
                .writer(chatMessageSaveDto.getWriter())
                .createdAt(chatMessageSaveDto.getCreatedAt())
                .chatroomNo(Long.parseLong(chatMessageSaveDto.getRoomNo()))
                .message(chatMessageSaveDto.getMessage())
                .build();
    }
}

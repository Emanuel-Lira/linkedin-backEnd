package com.unifacisa.linkedin.message.dto;

import com.unifacisa.linkedin.user.UserDTO;
import com.unifacisa.linkedin.message.Message;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private UserDTO sender; // Quem enviou
    private UserDTO recipient; // Quem recebeu

    public MessageResponseDTO(Message message, UserDTO senderDTO, UserDTO recipientDTO) {
        this.id = message.getId();
        this.content = message.getContent();
        this.sentAt = message.getSentAt();
        this.sender = senderDTO;
        this.recipient = recipientDTO;
    }
}

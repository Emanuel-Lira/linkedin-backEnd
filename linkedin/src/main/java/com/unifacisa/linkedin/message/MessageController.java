package com.unifacisa.linkedin.message;

import com.unifacisa.linkedin.message.dto.MessageCreateDTO;
import com.unifacisa.linkedin.message.dto.MessageResponseDTO;
import com.unifacisa.linkedin.user.User;
import com.unifacisa.linkedin.user.UserDTO;
import com.unifacisa.linkedin.user.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserServices userServices;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @RequestBody MessageCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User sender = userServices.findByEmail(userDetails.getUsername());
        Message newMessage = messageService.sendMessage(dto, sender);

        // Prepara o DTO de resposta
        UserDTO senderDTO = modelMapper.map(newMessage.getSender(), UserDTO.class);
        UserDTO recipientDTO = modelMapper.map(newMessage.getRecipient(), UserDTO.class);
        MessageResponseDTO response = new MessageResponseDTO(newMessage, senderDTO, recipientDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{recipientId}")
    public ResponseEntity<List<MessageResponseDTO>> getConversation(
            @PathVariable Long recipientId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User sender = userServices.findByEmail(userDetails.getUsername());
        List<Message> messages = messageService.getConversation(sender, recipientId);

        List<MessageResponseDTO> response = messages.stream().map(message -> {
            UserDTO senderDTO = modelMapper.map(message.getSender(), UserDTO.class);
            UserDTO recipientDTO = modelMapper.map(message.getRecipient(), UserDTO.class);
            return new MessageResponseDTO(message, senderDTO, recipientDTO);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}

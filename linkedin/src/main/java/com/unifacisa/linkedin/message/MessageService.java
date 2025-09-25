package com.unifacisa.linkedin.message;


import com.unifacisa.linkedin.message.dto.MessageCreateDTO;
import com.unifacisa.linkedin.user.User;
import com.unifacisa.linkedin.user.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserServices userServices; // serviço de user para buscar quem envia e quem recebe

    public Message sendMessage(MessageCreateDTO dto, User sender) {


        // Busca o destinatário no banco.
        User recipient = userServices.findById(dto.getRecipientId());

        Message message = new Message();
        message.setContent(dto.getContent());
        message.setSender(sender);
        message.setRecipient(recipient);

        return messageRepository.save(message);
    }

    public List<Message> getConversation(User user1, Long user2Id) {

        // Garantindo que o usuário 2 existe
        userServices.findById(user2Id);
        return messageRepository.findConversation(user1.getId(), user2Id);
    }

}

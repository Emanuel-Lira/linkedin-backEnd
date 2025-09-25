package com.unifacisa.linkedin.comment.dto;

import com.unifacisa.linkedin.comment.Comment;
import com.unifacisa.linkedin.user.UserDTO;
import lombok.Data;

@Data
public class CommentResponseDTO {

    private Long id;


    private String postContent;

    private String commentario;



    private UserDTO author;

    public CommentResponseDTO(Comment comment, UserDTO authorDTO) {
        this.id = comment.getId();


        this.postContent = comment.getPost().getContent();

        this.commentario = comment.getText();


        this.author = authorDTO;
    }
}
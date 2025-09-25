package com.unifacisa.linkedin.posts.dto;

import com.unifacisa.linkedin.posts.Post;
import com.unifacisa.linkedin.user.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserDTO author;


    public PostResponseDTO(Post post, UserDTO userDTO) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.author = userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }
}

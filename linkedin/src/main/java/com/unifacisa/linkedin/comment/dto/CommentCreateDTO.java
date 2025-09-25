package com.unifacisa.linkedin.comment.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

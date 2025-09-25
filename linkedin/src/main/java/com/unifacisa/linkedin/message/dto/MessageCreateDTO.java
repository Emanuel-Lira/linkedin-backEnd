package com.unifacisa.linkedin.message.dto;

public class MessageCreateDTO {
    private String content;
    private Long recipientId;
    // id de quem vai receber


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
}

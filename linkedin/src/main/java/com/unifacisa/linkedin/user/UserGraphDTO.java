package com.unifacisa.linkedin.user;


import lombok.Data;

@Data

public class UserGraphDTO {
    private Long id;
    private String name;

    public UserGraphDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

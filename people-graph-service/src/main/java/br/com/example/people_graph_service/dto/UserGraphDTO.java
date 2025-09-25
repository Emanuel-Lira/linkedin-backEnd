package br.com.example.people_graph_service.dto;

import lombok.Data;

@Data
public class UserGraphDTO {
    private Long id;
    private String name;

    public UserGraphDTO() {}



    public UserGraphDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

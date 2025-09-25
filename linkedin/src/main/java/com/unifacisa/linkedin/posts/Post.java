package com.unifacisa.linkedin.posts;


import com.unifacisa.linkedin.comment.Comment;
import lombok.Data;
import com.unifacisa.linkedin.user.User; // Importe sua
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
    @Entity
    @Table(name = "posts")
public class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(columnDefinition = "TEXT")
        private String content;

        private LocalDateTime createdAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User author;

        @PrePersist // Define a data de criação automaticamente antes de salvar
        public void prePersist() {
            createdAt = LocalDateTime.now();
        }

        @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Comment> comments = new ArrayList<>();

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

        public User getAuthor() {
            return author;
        }

        public void setAuthor(User author) {
            this.author = author;
        }
    }


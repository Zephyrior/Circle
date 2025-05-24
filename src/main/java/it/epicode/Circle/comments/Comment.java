package it.epicode.Circle.comments;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.posts.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Comments")

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "author_id")
    private AppUser author;

    @ManyToOne
    @JoinColumn(nullable = false, name = "post_id")
    private Post post;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
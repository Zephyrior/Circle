package it.epicode.Circle.likes;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.posts.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Likes")

public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",  nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private LocalDateTime likedAt;


    @PrePersist
    public void onLike() {
        likedAt = LocalDateTime.now();
    }
}
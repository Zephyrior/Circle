package it.epicode.Circle.comments;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.posts.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AppUser author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDate commentedAt;

}
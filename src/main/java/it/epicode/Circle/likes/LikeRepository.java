package it.epicode.Circle.likes;


import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(AppUser user, Post post);
//    long countByPost(Post post);
}
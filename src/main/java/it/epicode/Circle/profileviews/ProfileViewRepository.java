package it.epicode.Circle.profileviews;


import it.epicode.Circle.auth.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileViewRepository extends JpaRepository<ProfileView, Long> {
    List<ProfileView> findByViewed(AppUser user);
    boolean existsByViewerAndViewed(AppUser viewer, AppUser viewed);
}
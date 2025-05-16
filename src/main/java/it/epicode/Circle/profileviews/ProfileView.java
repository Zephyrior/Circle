package it.epicode.Circle.profileviews;

import it.epicode.Circle.auth.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ProfileViews")

public class ProfileView {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser viewer;

    @ManyToOne
    @JoinColumn(name = "viewed_user_id")
    private AppUser viewed;

    private LocalDate viewedAt;


}
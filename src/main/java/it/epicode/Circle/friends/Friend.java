package it.epicode.Circle.friends;

import it.epicode.Circle.auth.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Friends")

public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser requester;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private AppUser receiver;

}
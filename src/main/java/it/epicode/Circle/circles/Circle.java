package it.epicode.Circle.circles;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.enums.CircleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "circles")

public class Circle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private boolean smallCircle = false;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private AppUser requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private AppUser receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CircleStatus circleStatus = CircleStatus.PENDING;

}
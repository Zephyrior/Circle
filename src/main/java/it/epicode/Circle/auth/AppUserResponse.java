package it.epicode.Circle.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserResponse {

    private Long id;
    private String email;
    private String completeName;
    private LocalDate birthDate;
    private String profilePictureUrl;
    private String shoutOut;
    private String bio;
    private String hobby;
    private String location;
    private String nickName;
    private boolean setSmallCircleAsFeatured;
    private LocalDate createdAt;
    private Set<Role> role;
}

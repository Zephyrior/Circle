package it.epicode.Circle.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String profilePictureUrl;
    private LocalDate createdAt;
}

package it.epicode.Circle.auth;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
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

    @Size(max = 60, message = "Shout out must be less than 60 characters")
    private String shoutOut;

    @Size(max = 300, message = "Bio must be less than 300 characters")
    private String bio;
    private String profilePictureUrl;
}

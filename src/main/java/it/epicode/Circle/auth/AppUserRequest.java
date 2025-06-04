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
    private boolean smallCircleAsFeatured;

    @Size(max = 60, message = "Shout out must be less than 60 characters")
    private String shoutOut;

    @Size(max = 150, message = "Bio must be less than 150 characters")
    private String bio;
    private String profilePictureUrl;

    @Size(max = 60, message = "Hobby must be less than 60 characters")
    private String hobby;

    @Size(max = 20, message = "Location must be less than 20 characters")
    private String location;

    @Size(max = 20, message = "Nick name must be less than 20 characters")
    private String nickName;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;
}

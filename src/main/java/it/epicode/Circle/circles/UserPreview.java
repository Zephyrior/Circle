package it.epicode.Circle.circles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreview {
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
}

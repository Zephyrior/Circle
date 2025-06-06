package it.epicode.Circle.emails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class emailRequest {
    private String to;
    private String fullName;
}

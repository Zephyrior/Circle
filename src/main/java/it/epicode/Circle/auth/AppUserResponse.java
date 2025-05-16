package it.epicode.Circle.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserResponse {

    private Long id;
    private String email;
    private Set<Role> role;
}

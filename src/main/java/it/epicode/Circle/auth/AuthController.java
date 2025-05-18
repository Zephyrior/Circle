package it.epicode.Circle.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        appUserService.registerUser(
                registerRequest,

                Set.of(Role.ROLE_USER) // Assegna il ruolo di default
        );
        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = appUserService.authenticateUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Authorities: " + auth.getAuthorities());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping
    public List<AppUserResponse> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @GetMapping("/me")
    public AppUserResponse getCurrentUser() {
        return appUserService.getCurrentUser();
    }

//    @GetMapping("/me")
//    public ResponseEntity<?> me() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Auth /me: " + auth);
//        return ResponseEntity.ok(auth.getAuthorities());
//    }
}

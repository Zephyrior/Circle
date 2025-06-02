package it.epicode.Circle.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
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

    @GetMapping("/id/{id}")
    public AppUserResponse getUserById(@PathVariable Long id) {
        return appUserService.getUserById(id);
    }

//    @GetMapping("/me")
//    public ResponseEntity<?> me() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Auth /me: " + auth);
//        return ResponseEntity.ok(auth.getAuthorities());
//    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update")
    public AppUserResponse updateUser(@RequestBody AppUserRequest request) {
        return appUserService.updateUser(request);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/shout-out")
    public AppUserResponse updateShoutOut(@RequestBody AppUserRequest request) {
        return appUserService.updateShoutOut(request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/find-users")
    public List<AppUserResponse> findUsersByName(@RequestParam String name) {
        return appUserService.findUsersByName(name);
    }
}

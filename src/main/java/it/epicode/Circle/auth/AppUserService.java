package it.epicode.Circle.auth;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public AppUser registerUser(RegisterRequest request,Set<Role> roles) {
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email già registrata");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setBirthDate(request.getBirthDate());
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setProfilePicture("https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=200");
        appUser.setRoles(roles);
        return appUserRepository.save(appUser);
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public String authenticateUser(String email, String password)  {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            throw new SecurityException("Credenziali non valide", e);
        }
    }


    public AppUser loadUserByEmail(String email)  {
        AppUser appUser = appUserRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con l'email: " + email));


        return appUser;
    }

    public List<AppUserResponse> getAllUsers() {
        return appUserRepository.findAll().stream()
                .map(a -> new AppUserResponse(a.getId(),  a.getEmail(), a.getRoles()))
                .collect(Collectors.toList());
    }
}

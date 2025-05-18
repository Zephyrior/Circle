package it.epicode.Circle.auth;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public AppUser registerUser(RegisterRequest request,Set<Role> role) {
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email già registrata");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setBirthDate(request.getBirthDate());
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setProfilePictureUrl("https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=200");
        appUser.setRole(role);
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
            throw new SecurityException("Invalid credentials", e);
        }
    }


    public AppUser loadUserByEmail(String email)  {
        AppUser appUser = appUserRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User with email : " + email + " not found"));


        return appUser;
    }

    public List<AppUserResponse> getAllUsers() {
        return appUserRepository.findAll().stream()
                .map(a -> new AppUserResponse(a.getId(),  a.getEmail(), a.getFirstName()+" "+a.getLastName(), a.getBirthDate(), a. getProfilePictureUrl(), a.getCreatedAt(), a.getRole()))
                .collect(Collectors.toList());
    }

//    public AppUserResponse getUserById(Long id) {
//        AppUser appUser = appUserRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        return new AppUserResponse(appUser.getId(), appUser.getEmail(), appUser.getFirstName()+" "+appUser.getLastName(), appUser.getBirthDate(), appUser.getProfilePictureUrl(), appUser.getCreatedAt(), appUser.getRoles());
//    }

    public AppUser getUserByEmail() {
        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser  appUser= (AppUser) authentication.getPrincipal();

        return appUser;
    }

    public AppUserResponse getCurrentUser() {
        AppUser appUser = getUserByEmail();

        return new AppUserResponse(appUser.getId(), appUser.getEmail(), appUser.getFirstName()+" "+appUser.getLastName(), appUser.getBirthDate(), appUser.getProfilePictureUrl(), appUser.getCreatedAt(), appUser.getRole());
    }

//    public AppUser getCurrentUser() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof AppUser) {
//            return (AppUser) principal;
//        } else if (principal instanceof UserDetails) {
//            String email = ((UserDetails) principal).getUsername();
//            return appUserRepository.findByEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//        } else if (principal instanceof String) {
//            return appUserRepository.findByEmail((String) principal)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + principal));
//        } else {
//            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
//        }
//    }
}

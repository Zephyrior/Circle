package it.epicode.Circle.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Creazione dell'utente admin se non esiste
        Optional<AppUser> adminUser = appUserService.findByEmail("jackoat2001@gmail.com");
        if (adminUser.isEmpty()) {
            RegisterRequest adminRequest = new RegisterRequest();
            adminRequest.setEmail("jackoat2001@gmail.com");
            adminRequest.setPassword("adminpwd");
            adminRequest.setFirstName("Maz");
            adminRequest.setLastName("Tev");
            adminRequest.setBirthDate(LocalDate.of(1994, 1, 20));
            appUserService.registerUser(adminRequest, Set.of(Role.ROLE_ADMIN));
        }

        // Creazione dell'utente user se non esiste
        Optional<AppUser> normalUser = appUserService.findByEmail("user@mail.com");
        if (normalUser.isEmpty()) {
            RegisterRequest userRequest = new RegisterRequest();
            userRequest.setEmail("user@mail.com");
            userRequest.setPassword("userpswd");
            userRequest.setFirstName("Xav");
            userRequest.setLastName("Tez");
            userRequest.setBirthDate(LocalDate.of(1994, 1, 20));
            appUserService.registerUser(userRequest, Set.of(Role.ROLE_USER));
        }
    }
}

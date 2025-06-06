package it.epicode.Circle.emails;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
public class emailController {

    @Autowired
    private EmailSenderService emailSenderService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/registration")
    public ResponseEntity<String> sendRegistrationEmail( @RequestBody emailRequest request) {
        try {
            emailSenderService.sendEmailRegistration(request);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User registered, but email failed to send.");
        }

        return ResponseEntity.ok("User registered and welcome email sent.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public ResponseEntity<String> sendUpdateProfile(@RequestBody emailRequest request) {
        try {
            emailSenderService.sendUpdateProfile(request);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Profile updated, but email failed to send.");
        }

        return ResponseEntity.ok("Profile updated and email sent.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/invitation")
    public ResponseEntity<String> sendInvitation(@RequestBody emailRequest request) {
        try {
            emailSenderService.sendInvitation(request);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Invitation sent, but email failed to send.");
        }

        return ResponseEntity.ok("Invitation sent and email sent.");
    }
}

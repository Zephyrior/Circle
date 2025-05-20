package it.epicode.Circle.circles;


import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.auth.AppUserRepository;
import it.epicode.Circle.enums.CircleStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class CircleService {

    @Autowired
    private CircleRepository circleRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUser) {
            return ((AppUser) principal).getEmail();
        } else {
            return principal.toString();
        }
    }

    public AppUser getUserByEmail() {
        String email = getCurrentUserEmail();
        return appUserRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
    }

    public List<CircleResponse> getAcceptedCirclesFromCurrentUser() {
        Long userId = getUserByEmail().getId();
        List<Circle> circles = circleRepository.findAcceptedCirclesByUserId(userId);

        return circles.stream().map(c -> {
            AppUser otherUser = c.getReceiver().getId().equals(userId) ? c.getRequester() : c.getReceiver();
            return new CircleResponse(c.getId(),
                    new UserPreview(otherUser.getId(),
                            otherUser.getFirstName(),
                            otherUser.getLastName(),
                            otherUser.getProfilePictureUrl()
                    ),
                    c.isSmallCircle(),
                    c.getCircleStatus());
        }).toList();
    }

    public List<CircleResponse> getCirclesByStatus(CircleStatus status) {

        Long userId = getUserByEmail().getId();

        List<Circle> circles = circleRepository.getCirclesByStatus(status, userId);
        return circles.stream().map(c -> {
            AppUser otherUser = c.getReceiver().getId().equals(userId) ? c.getRequester() : c.getReceiver();
            return new CircleResponse(c.getId(),
                    new UserPreview(otherUser.getId(),
                            otherUser.getFirstName(),
                            otherUser.getLastName(),
                            otherUser.getProfilePictureUrl()
                    ),
                    c.isSmallCircle(),
                    c.getCircleStatus());
        }).toList();
    }

    public List<CircleResponse> getSmallCircles() {
        Long userId = getUserByEmail().getId();
        List<Circle> circles = circleRepository.findSmallCircles(userId);
        return circles.stream().map(c -> {
            AppUser otherUser = c.getReceiver().getId().equals(userId) ? c.getRequester() : c.getReceiver();
            return new CircleResponse(c.getId(),
                    new UserPreview(otherUser.getId(),
                            otherUser.getFirstName(),
                            otherUser.getLastName(),
                            otherUser.getProfilePictureUrl()
                    ),
                    c.isSmallCircle(),
                    c.getCircleStatus());
        }).toList();
    }
}

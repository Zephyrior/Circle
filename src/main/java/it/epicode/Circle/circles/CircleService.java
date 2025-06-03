package it.epicode.Circle.circles;


import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.auth.AppUserRepository;
import it.epicode.Circle.enums.CircleStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

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
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
    }

    private UserPreview toUserPreview(AppUser user) {
        return new UserPreview(user.getId(), user.getFirstName(), user.getLastName(), user.getProfilePictureUrl());
    }

    private CircleResponse toCircleResponse(Circle c) {
        return new CircleResponse(
                c.getId(),
                toUserPreview(c.getRequester()),
                toUserPreview(c.getReceiver()),
                c.isSmallCircle(),
                c.getCircleStatus()
        );
    }

    public List<CircleResponse> getAcceptedCirclesFromCurrentUser() {
        Long userId = getUserByEmail().getId();
        List<Circle> circles = circleRepository.findAcceptedCirclesByUserId(userId);

        return circles.stream().map(this::toCircleResponse).toList();
    }

    public List<CircleResponse> findAcceptedCirclesByUserId( Long id) {
        List<Circle> circles = circleRepository.findAcceptedCirclesByUserId(id);

        return circles.stream().map(this::toCircleResponse).toList();
    }

    public List<CircleResponse> getCirclesByStatus(CircleStatus status) {

        Long userId = getUserByEmail().getId();

        List<Circle> circles = circleRepository.getCirclesByStatus(status, userId);
        return circles.stream().map(this::toCircleResponse).toList();
    }

    public List<CircleResponse> getSmallCircles() {
        Long userId = getUserByEmail().getId();
        List<Circle> circles = circleRepository.findSmallCircles(userId);
        return circles.stream().map(this::toCircleResponse).toList();
    }

    @Transactional
    public CircleResponse sendCircleRequest(Long receiverId) {
        AppUser requester = getUserByEmail();

        if (requester.getId().equals(receiverId)) {
            throw new IllegalArgumentException("You cannot send a circle request to yourself");
        }

        AppUser receiver = appUserRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + receiverId));

        Optional<Circle> existingCircle = circleRepository
                .findExistingCircleRequest(requester.getId(), receiver.getId());

        if (existingCircle.isPresent()) {
            throw new IllegalArgumentException("Circle request already exists");
        }

        Circle newCircle = new Circle();
        newCircle.setRequester(requester);
        newCircle.setReceiver(receiver);
        newCircle.setCircleStatus(CircleStatus.PENDING);
        newCircle.setSmallCircle(false);

        Circle savedCircle = circleRepository.save(newCircle);

        return toCircleResponse(savedCircle);
    }

    @Transactional
    public CircleResponse acceptCircleRequest(Long circleId) {
        AppUser receiver = getUserByEmail();
        Circle circle = circleRepository.findById(circleId)
                .orElseThrow(() -> new EntityNotFoundException("Circle not found: " + circleId));

        if (!circle.getReceiver().getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("You are not the receiver of this circle request");
        }

        circle.setCircleStatus(CircleStatus.ACCEPTED);
        Circle savedCircle = circleRepository.save(circle);

        return toCircleResponse(savedCircle);
    }

    @Transactional
    public void declineCircleRequest(Long circleId) {
        AppUser receiver = getUserByEmail();
        Circle circle = circleRepository.findById(circleId)
                .orElseThrow(() -> new EntityNotFoundException("Circle not found: " + circleId));

        if (!circle.getReceiver().getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("You are not the receiver of this circle request");
        }

        if (circle.getCircleStatus() != CircleStatus.PENDING) {
            throw new IllegalArgumentException("Only pending circle requests can be declined");
        }
        circleRepository.delete(circle);
    }

    @Transactional
    public void cancelCircleRequest(Long circleId) {
        AppUser requester = getUserByEmail();
        Circle circle = circleRepository.findById(circleId)
                .orElseThrow(() -> new EntityNotFoundException("Circle not found: " + circleId));

        if (!circle.getRequester().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("You are not the requester of this circle request");
        }

        if (circle.getCircleStatus() != CircleStatus.PENDING) {
            throw new IllegalArgumentException("Only pending circle requests can be declined");
        }
        circleRepository.delete(circle);
    }

    public Optional<CircleResponse> getCircleBetweenUsers(Long userId1, Long userId2) {
        Optional<Circle> circle = circleRepository.findExistingCircleRequest(userId1, userId2);
        return circle.map(this::toCircleResponse);
    }
}

package it.epicode.Circle.circles;

import it.epicode.Circle.enums.CircleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/circles")
@Validated
public class CircleController {

    @Autowired
    private CircleService circleService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mycircles")
    public List<CircleResponse> getAcceptedCirclesFromCurrentUser() {
        return circleService.getAcceptedCirclesFromCurrentUser();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usercircles")
    public List<CircleResponse> getAcceptedCirclesByUserId(@RequestParam("id") Long id) {
        return circleService.findAcceptedCirclesByUserId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<CircleResponse> getCirclesByStatus(@RequestParam("status") CircleStatus status) {
        return circleService.getCirclesByStatus(status);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mysmallcircle")
    public List<CircleResponse> getSmallCircles() {
        return circleService.getSmallCircles();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addcircle/{receiverId}")
    public CircleResponse sendCircleRequest(@PathVariable Long receiverId) {
        return circleService.sendCircleRequest(receiverId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/acceptcircle/{circleId}")
    public CircleResponse acceptCircleRequest(@PathVariable Long circleId) {
        return circleService.acceptCircleRequest(circleId);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/cancelcircle/{circleId}")
    public ResponseEntity<?> cancelCircleRequest(@PathVariable Long circleId) {
        circleService.cancelCircleRequest(circleId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/declinecircle/{circleId}")
    public ResponseEntity<?> declineCircleRequest(@PathVariable Long circleId) {
        circleService.declineCircleRequest(circleId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/circle/{userId1}/{userId2}")
    public ResponseEntity<CircleResponse> getCircleBetweenUsers(@PathVariable Long userId1, @PathVariable Long userId2) {
        Optional<CircleResponse> circle = circleService.getCircleBetweenUsers(userId1, userId2);
        return circle.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

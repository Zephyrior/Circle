package it.epicode.Circle.circles;

import it.epicode.Circle.enums.CircleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/circles")
public class CircleController {

    @Autowired
    private CircleService circleService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mycircles")
    public List<CircleResponse> getAcceptedCirclesFromCurrentUser() {
        return circleService.getAcceptedCirclesFromCurrentUser();
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


}

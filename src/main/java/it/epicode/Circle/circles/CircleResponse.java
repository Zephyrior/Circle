package it.epicode.Circle.circles;

import it.epicode.Circle.enums.CircleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CircleResponse {
    private Long id;
    private UserPreview circle;
    private boolean smallCircle;
    private CircleStatus status;
}

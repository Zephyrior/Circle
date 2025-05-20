package it.epicode.Circle.circles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CircleRequest {

    private Long requesterId;
    private Long receiverId;
    private boolean smallCircle;

}

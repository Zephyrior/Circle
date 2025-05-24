package it.epicode.Circle.likes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponse {

    private Long id;
    private Long userId;
    private String userFullName;

}

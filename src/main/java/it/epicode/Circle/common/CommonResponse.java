package it.epicode.Circle.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    private Long id;
    private String message;


    public static CommonResponse withId(Long id) {
        return new CommonResponse(id, null);
    }

    public static CommonResponse withMessage(String message) {
        return new CommonResponse(null, message);
    }
}

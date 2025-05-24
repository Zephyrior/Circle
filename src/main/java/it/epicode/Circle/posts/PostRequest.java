package it.epicode.Circle.posts;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @NotBlank(message = "Content is required")
    private String content;

    private String mediaUrl;

    private Long profileOwnerId;

}

package it.epicode.Circle.posts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String content;
    private String mediaUrl;
    private LocalDate createdAt;
    private Long authorId;
    private List<Long> commentIds = new ArrayList<>();
    private List<Long> likeIds = new ArrayList<>();
}

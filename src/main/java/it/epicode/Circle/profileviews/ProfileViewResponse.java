package it.epicode.Circle.profileviews;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileViewResponse {

    private Long id;
    private Long viewerId;
    private String viewerFullName;
    private String viewerProfilePictureUrl;
    private String viewedAt;
}

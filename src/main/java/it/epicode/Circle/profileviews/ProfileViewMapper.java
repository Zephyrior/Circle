package it.epicode.Circle.profileviews;


import java.time.format.DateTimeFormatter;

public class ProfileViewMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static ProfileViewResponse toProfileViewResponse(ProfileView profileView) {
        ProfileViewResponse response = new ProfileViewResponse();
        response.setId(profileView.getId());
        response.setViewerId(profileView.getViewer().getId());
        response.setViewerFullName(profileView.getViewer().getFirstName() + " " + profileView.getViewer().getLastName());
        response.setViewerProfilePictureUrl(profileView.getViewer().getProfilePictureUrl());
        response.setViewedAt(profileView.getViewedAt().format(formatter));
        return response;
    }
}

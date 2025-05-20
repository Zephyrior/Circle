package it.epicode.Circle.auth;

import it.epicode.Circle.comments.Comment;
import it.epicode.Circle.circles.Circle;
import it.epicode.Circle.likes.Like;
import it.epicode.Circle.posts.Post;
import it.epicode.Circle.widgets.Widget;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String profilePictureUrl;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

//    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private List<Circle> sentRequests = new ArrayList<>();
//
//    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private List<Circle> receivedRequests = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Widget> widgets = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    private  boolean accountNonExpired=true;
    private  boolean accountNonLocked=true;
    private  boolean credentialsNonExpired=true;
    private  boolean enabled=true;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        return role.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }

//    public AppUser(String email, String password, Collection<? extends GrantedAuthority> authorities, Set<Role> role) {
//        this(email, password, true, true, true, true, authorities, role);
//    }
//
//    public AppUser(String email, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Set<Role> role) {
//        this.email = email;
//        this.password = password;
//        this.enabled = enabled;
//        this.accountNonExpired = accountNonExpired;
//        this.credentialsNonExpired = credentialsNonExpired;
//        this.accountNonLocked = accountNonLocked;
//        this.role = role;
//   }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

   @Override
    public String getUsername() {
        return this.email;
    }


}

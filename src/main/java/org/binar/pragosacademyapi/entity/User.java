package org.binar.pragosacademyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binar.pragosacademyapi.enumeration.Role;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String city;
    private String country;
    @Column(name = "image_profile")
    private String imageProfile;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "is_enable")
    private Boolean isEnable;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserVerification userVerification;
}

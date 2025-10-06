package com.osr.account; 

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    public UUID profile_id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false)
    public String password;
    
    @Column(nullable = false)
    public String role;

    public String toString() {
	return String.format("Profile(username=%1$s, password=%2$s, role=%3$s)", username, password, role);
    }
}

interface ProfileRepository extends JpaRepository<Profile, UUID> {}

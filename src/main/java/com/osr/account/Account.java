package com.osr.account; 

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false)
    public String password;
    
    @Column(nullable = false)
    public String role;

    @OneToMany(mappedBy="profileId")
    public List<Character> characters;

    public String toString() {
	return String.format("Profile(username=%1$s, password=%2$s, role=%3$s)", username, password, role);
    }
}

interface AccountRepository extends JpaRepository<Account, UUID> {
    //Account findOneByUsername();
}

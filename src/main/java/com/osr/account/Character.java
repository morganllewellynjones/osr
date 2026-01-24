package com.osr.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Entity
public class Character {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    public UUID profileId;

    @Column(nullable = false)
    public String name;

    public String toString() {
	return String.format("Character(name=%1$s, profile_id=%2$s)", name, profileId);
    }
}

interface CharacterRepository extends JpaRepository<Account, UUID> {}

package com.osr.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import jakarta.persistence.FetchType; 

@Entity
public class Character {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    public UUID id;

    @JoinColumn(nullable = false, name="accountId")
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    public Account account;

    @Column(nullable = false)
    public String name;

    public String toString() {
	return String.format("Character(id=%1$s, name=%2$s, account_id=%3$s)", id, name, account.id);
    }

    Account getAccount() { return this.account; }

    public Character() {}
    
    public Character(String name, Account account) {
	this.name = name;
	this.account = account;
    }
}

interface CharacterRepository extends JpaRepository<Character, UUID> {
    Character findByName(String name);
}

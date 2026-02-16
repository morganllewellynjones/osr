package com.osr.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import jakarta.persistence.FetchType; 
import org.hibernate.annotations.NaturalId;

@Entity
public class Character {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    public UUID id;

    @JoinColumn(name="accountId")
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @NaturalId
    public Account account;

    @NaturalId
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

    @Override
    public boolean equals(Object other) {
	return other instanceof Character 
	    && ((Character) other).account.id == account.id
	    && ((Character) other).name == name;
    } 

    @Override
    public int hashCode() {
	return name.hashCode() * account.id.hashCode();
    }
}

interface CharacterRepository extends JpaRepository<Character, UUID> {
    Character findByName(String name);
}

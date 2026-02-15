package com.osr.account; 

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import org.hibernate.annotations.NaturalId;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    @NaturalId
    public String username;

    @Column(nullable = false)
    public String password;
    
    @Column(nullable = false)
    public String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="account")
    public List<Character> characters = new ArrayList<Character>();

    public String toString() {
	return String.format("Profile(id=%1$s, username=%2$s, password=%3$s, role=%4$s)", id, username, password, role);
    }

    public Account() {}

    public Account(String username, String password, String role) {
	this.username = username;
	this.password = password;
	this.role = role;
    }
    
    public Account(UUID id, String username, String password, String role) {
	this.id = id;
	this.username = username;
	this.password = password;
	this.role = role;
    }

    @Override
    public boolean equals(Object o) {
	if ( this == o ) { return true; }
	if (o == null || getClass() != o.getClass()) { return false; }
	Account account = (Account) o;
	return username != null ? username.equals(account.username) : account.username == null;
    }
}

interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByUsername(String username);
    List<Account> findAllByRole(String role);
}

package com.osr.views;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity @Immutable
@Subselect("SELECT a.username,c.name FROM account a JOIN character c ON a.id = c.account_id")
public class PlayerInfo {
    @Id
    @Column(name = "username")
    public String accountUsername;

    @Column(name = "name")
    public String charName;
};

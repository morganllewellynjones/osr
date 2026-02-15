package com.osr.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import com.osr.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.stat.Statistics;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.Arrays;
import java.util.UUID;

import jakarta.persistence.EntityManager;


@DataJpaTest
class AccountTests {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CharacterRepository characterRepository;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private EntityManager entityManager;

	private static final Logger logger = Logger.getLogger(AccountTests.class.getName());

	@BeforeEach
	public void setup() {
	    setupData();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void setupData() {
	    Account a = new Account("donkey_kong", "snowflake", "player");
	    Character arthur = new Character("arthur", a);
	    Character merlin = new Character("merlin", a);
	    Character viviane = new Character("viviane", a);
	    accountRepository.save(a);
	    characterRepository.saveAll(List.of(arthur, merlin, viviane));
	    entityManager.flush();
	    entityManager.clear();
	}

	@Test
	@Transactional
	@DisplayName("")
	void accountWithCharacters() {
	    logger.info("Fetching Donkey Kong");
	    var account = accountRepository.findByUsername("donkey_kong");
	    logger.info("Finished with Donkey Kong!");
	}

	@Test
	@Transactional
	@DisplayName("")
	void testEquality() {
	    Account a = new Account("donkey_kong", "new_password", "new_role");
	    Account b = accountRepository.findByUsername("donkey_kong");
	    logger.info("Does a = b?");
	    assert(a.equals(b) == true);

	    //accountRepository.saveAndFlush(a);
	    //assert(b.password == "new_password");
	}

	@Test
	@Transactional
	@DisplayName("Merge requires no previously inserted object")
	void testMergeNew() {
	    var newAccountName = "bowser_boy";
	    var a = new Account(newAccountName, "new_password", "new_role");
	    logger.info(String.format("Merging %s", newAccountName));
	    entityManager.merge(a);
	    logger.info(String.format("Fetching %s", newAccountName));
	    var b = accountRepository.findByUsername(newAccountName);
	    assert(b.role == "new_role");
	}

	@Test
	@Transactional
	@DisplayName("Merge on a persistent object updates state in db")
	void testMergeDirty() {
	    logger.info("Getting base account...");
	    var a = accountRepository.findByUsername("donkey_kong");
	    logger.info("Updating account details...");
	    a.password = "new_password";
	    a.role = "new_role";
	    logger.info("Merging data...");
	    logger.info("Fetching new account...");
	    var b = accountRepository.findByUsername("donkey_kong");
	    assert(b.password == "new_password");
	    assert(b.role == "new_role");
	    logger.info(b.toString());
	}

	@Test
	@Transactional
	@DisplayName("Test to see if loading an entity and modifying its state causes a flush without an explicit save.")
	void testDirtyUpdateWithoutSaving() {
	    Account a = accountRepository.findByUsername("donkey_kong");
	    a.password = "new_password";
	    a.role = "new_role";
	    logger.info("Will it flush?");
	}

	@Test
	@Transactional
	@DisplayName("Test upserting values that violate unique constraint")
	void testOnConflictInsert() {

	    logger.info("Fetching stateless session handle");
	    var session = entityManager.unwrap(Session.class);	
	    logger.info("Setting jdbc batch size");
	    session.setJdbcBatchSize(30);

	    var newAccounts = List.of(
		    new Account("new_1", "pass", "player"),
		    new Account("new_2", "pass", "player"),
		    new Account("new_3", "pass", "player"),
		    new Account("donkey_kong", "pass", "player")
	    );

	    logger.info("Creating the new accounts");
	    newAccounts.forEach(a -> {
		entityManager.createQuery("""
			INSERT INTO Account (username, password, role)
			VALUES (:username, :password, :role)
			ON CONFLICT (username) DO UPDATE
			SET password = :password
			""")
		    .setParameter("username", a.username)
		    .setParameter("password", a.password)
		    .setParameter("role", a.role)
		    .executeUpdate();
	    });


	    logger.info("Fetching accounts");
	    var allAccounts = accountRepository.findAll();
	    
	    allAccounts.forEach(a -> logger.info(a.toString()));
	    logger.info("Finished testing upsert");
	}

}

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
	
	@Transactional
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
	@DisplayName("Test to see that a newly created account is equal to an existing persistent account with the same name.")
	void testEquality() {
	    Account a = new Account("donkey_kong", "new_password", "new_role");
	    Account b = accountRepository.findByUsername("donkey_kong");
	    logger.info(String.format("Does a = b? %b", a.equals(b)));
	    assert(a.equals(b) == true);
	}

	@Test
	@Transactional
	@DisplayName("Test that hibernate properly detects a mutation to a persistent entity and flushes state without an explicity update.")
	void testDirtyUpdate() {
	    logger.info("Fetching first instance of donkey_kong");
	    var a = accountRepository.findByUsername("donkey_kong");
	    assert(a.password == "snowflake");
	    logger.info("Fetching second instance of donkey_kong");
	    var b = accountRepository.findByUsername("donkey_kong");
	    logger.info("Changing password");
	    b.password = "new_password";
	    logger.info("Flushing state without explicitly updating the entity in the database");
	    entityManager.flush();
	    logger.info("Fetching same entity after a flush");
	    var c = accountRepository.findByUsername("donkey_kong");
	    logger.info("Checking that new reference to donkey_kong has the updated password");
	    assert(c.password == "new_password");
	}

	@Test
	@Transactional
	void testCompetingDirtyUpdates() {
	    logger.info("Fetching first instance of donkey_kong");
	    var a = accountRepository.findByUsername("donkey_kong");
	    assert(a.password == "snowflake");
	    assert(a.role == "player");

	    logger.info("Changing password and role from first reference");
	    a.password = "new_password";
	    a.role = "new_role";
	    logger.info("Fetching second instance of donkey_kong");

	    var b = accountRepository.findByUsername("donkey_kong");

	    logger.info("Overwriting new password from second reference");

	    b.password = "other_new_password";
	    
	    logger.info("Flushing state without explicitly updating the entity in the database");
	    entityManager.flush();
	    
	    logger.info("Fetching same entity after a flush");
	    var c = accountRepository.findByUsername("donkey_kong");
	    logger.info("Checking that new reference to donkey_kong has the updated password");
	    assert(c.password == "other_new_password");
	    assert(c.role == "new_role");
	}

	@Test
	@Transactional
	@DisplayName("Test to see if loading an entity and modifying its state causes a flush without an explicit save.")
	void testDirtyUpdateWithoutSaving() {
	    Account a = accountRepository.findByUsername("donkey_kong");
	    a.password = "new_password";
	    a.role = "new_role";

	    //It seems not. If you explicitly flush or call an action that requires a flush it will (for example a native query).
	    logger.info("Will it flush?");
	}

	@Test
	@Transactional
	@DisplayName("Test safely upserting values that violate unique constraint.")
	void testOnConflictInsert() {

	    logger.info("Fetching stateless session handle");
	    var session = entityManager.unwrap(Session.class);	
	    logger.info("Setting jdbc batch size");
	    session.setJdbcBatchSize(30);

	    var newAccounts = List.of(
		    new Account("new_1", "pass", "player"),
		    new Account("new_2", "pass", "player"),
		    new Account("new_3", "pass", "player"),
		    new Account("donkey_kong", "new_pass", "new_role")
	    );

	    logger.info("Creating the new accounts");
	    newAccounts.forEach(a -> {
		entityManager.createQuery("""
			INSERT INTO Account (username, password, role)
			VALUES (:username, :password, :role)
			ON CONFLICT (username) DO UPDATE
			SET 
			    password = :password,
			    role = :role
			""")
		    .setParameter("username", a.username)
		    .setParameter("password", a.password)
		    .setParameter("role", a.role)
		    .executeUpdate();
	    });


	    logger.info("Fetching accounts");
	    var allAccounts = accountRepository.findAll();
	    var updatedAccount = accountRepository.findByUsername("donkey_kong");
	    assert(updatedAccount.password == "new_pass");
	    assert(updatedAccount.role == "new_role");
	    
	    allAccounts.forEach(a -> logger.info(a.toString()));
	    logger.info("Finished testing upsert");
	}

}

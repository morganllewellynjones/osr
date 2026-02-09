package com.osr.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
//import org.springframework.boot.test.context.SpringBootTest;
import com.osr.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.Session;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.Arrays;

import com.osr.views.PlayerInfo;

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
	
//	@Test
//	@Disabled("Was used for setup")
//	public void createAccountWithCharacters() {
//
//	    logger.info("STARTED FUNCTION");
//	    var player = new Account();
//	    player.username = "donkey kong";
//	    player.password = "bluejay";
//	    player.role = "player";
//	    accountRepository.save(player);
//	    
//	    var arthur = new Character();
//	    var merlin = new Character();
//	    var viviane = new Character();
//	    arthur.name = "arthur";
//	    merlin.name = "merlin";
//	    viviane.name = "viviane";
//	    List.of(arthur,merlin,viviane).forEach(c -> c.account = player);
//	    accountRepository.save(player);
//	    player.characters.addAll(List.of(arthur,merlin,viviane));
//
//	    characterRepository.save(arthur);
//	    characterRepository.save(merlin);
//	    characterRepository.save(viviane);
//
//	    logger.info("FINISHED FUNCTION");
//	}
//
//
//	@Test
//	@Disabled("Not concerned with this test right now")
//	void createAccount() {
//
//
//	    var admin = new Account();
//	    Assertions.assertAll("account",
//		() -> Assertions.assertNull(admin.id),
//		() -> Assertions.assertNull(admin.username),
//		() -> Assertions.assertNull(admin.password)
//	    );
//
//	    admin.username = "morgan_admin";
//	    admin.password = "bluejay_admin";
//	    admin.role = "admin";
//	    
//	    var player = new Account();
//	    player.username = "morgan";
//	    player.password = "bluejay";
//	    player.role = "player";
//
//	    var player2 = new Account();
//	    player2.username = "donkey_kong";
//	    player2.password = "king_kong";
//	    player2.role = "player";
//
//
//	    accountRepository.save(admin);
//	    accountRepository.save(player);
//	    accountRepository.save(player2);
//
//	    var p = accountRepository.findByUsername("morgan_admin");
//	    Assertions.assertTrue(p.username == "morgan_admin");
//	    Assertions.assertTrue(p.password == "bluejay_admin");
//	    Assertions.assertTrue(p.role == "admin");
//
//	}
//
//	@Test
//	@Transactional
//	@Disabled
//	void accessCharacters() {
//
//	    List<Account> players = accountRepository.findAllByRole("player");
//	    players.forEach(p -> {
//		    p.characters.forEach(c -> logger.info(c.toString()));
//	    });
//	    logger.info("Finished");
//	}
//
//	@Test
//	@Transactional
//	@Disabled
//	void accessCharacterIds() {
//	    List<Account> players = accountRepository.findAllByRole("player");
//	    players.forEach(p -> {
//		p.characters.forEach(c -> logger.info(c.id.toString()));
//	    });
//	}
//
//	@Test
//	@Transactional
//	@Disabled
//	void accessCharacterSize() {
//	    Account p = accountRepository.findByUsername("donkey kong");
//	    logger.info(String.format("isEmpty: %b", p.characters.isEmpty()));
//	    //logger.info(String.format("Size: %d", p.characters.size()));
//	}
//
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
//
//	@Test
//	@Transactional
//	@Disabled
//	void accountProxyByCharacter() {
//	    setupData();
//	    entityManager.flush();
//	    entityManager.clear();
//	    Character c = characterRepository.findByName("viviane");
//
//	    logger.info(String.format("Account id: %s", c.getAccount().getId().toString()));
//
//	    var account = new Account();
//	    logger.info(String.format("Account is equal to empty proxy: %b", c.account.equals(account)));
//
//	    logger.info(String.format("Account role is: %s", c.account.role));
//	    logger.info(String.format("Account name is: %s", c.account.getUsername()));
//	    logger.info(String.format("Account details is: %s", c.account.toString()));
//	    
//	}
//
//	@Test
//	@Transactional
//	void dtoTest() {
//	    setupData();
//
//	}

	@Test
	@Transactional
	void accountWithCharacters() {
	    logger.info("Fetching Donkey Kong");
	    var account = accountRepository.findByUsername("donkey_kong");


	    logger.info("Fetching player info");
	    List<PlayerInfo> playerInfo = entityManager.createQuery(
		    "SELECT pi FROM PlayerInfo pi WHERE pi.accountUsername = 'donkey_kong'"
		    ).getResultList();
	    playerInfo.forEach(
		    p -> {
			logger.info(String.format("Player: %s, Character %s", p.accountUsername,p.charName));
		    }
		);
	    logger.info("All player info fetched");

	    
	    //List<PlayerInfo> playerInfo = entityManager.createQuery(
	//	    "SELECT a.username blah,c.name bar FROM Account a JOIN a.characters c",
	//	    PlayerInfo.class).getResultList();
	    //logger.info("Setting account to Mario");
	    //logger.info("Selecting random number");
	    //var r = entityManager.createNativeQuery("SELECT 1").getResultList();
	    //logger.info(r.toString());

	    //Session session = entityManager.unwrap(Session.class);
	    //session.
	    //logger.info("Fetching account with previous name");
	    //var secondAccount = accountRepository.findByUsername("donkey_kong");
	    //logger.info(String.format("Account is ditry? %b", !account.equals(secondAccount)));
	    //accountRepository.save(account);
	    //logger.info(HibernateProxy.extractLazyInitializer(account.characters).toString());
	    //var arthur = Hibernate.get(account.characters, 0);
	    //logger.info(String.format("Arthur is initialized? %b", Hibernate.isInitialized(arthur)));
	    //logger.info(String.format("Character are initialized? %b", Hibernate.isInitialized(c)));
	    //logger.info(String.format("Character size is? %d", Hibernate.size(c)));
	    //logger.info(String.format("Character are initialized? %b", Hibernate.isInitialized(c)));
	    logger.info("Finished with Donkey Kong!");
	    //logger.info(account.characters.toString());
	    //logger.info(String.format("Characters: %d", account.characters.size()));
	}

//	@Test
//	@Transactional
//	@Disabled
//	void charactersWithAccounts() {
//	    setupData();
//	    logger.info("Fetching arthur");
//	    var character = characterRepository.findByName("arthur");
//	    //logger.info(character.toString());
//	    //logger.info(Hibernate.getClass(character.account).toString());
//	    //logger.info(String.format("Account id: %s", character.account.id.toString()));
//	    logger.info("Finished with Arthur!");
//
//	}

//	void accessCharactersHibernate() {
//
//	    sessionFactory.inTransaction(s -> (
//		List<Account> players = s.createQuery("SELECT a FROM Account a WHERE a.role = 'player'", Account.class)
//		    .getResultList();
//		players.forEach(p -> {
//		    p.characters.forEach(c -> logger.info(c.toString()));
//		    )
//
//	    })
//	}

}

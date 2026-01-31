package com.osr.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import com.osr.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.Arrays;


@SpringBootTest
class AccountTests {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CharacterRepository characterRepository;

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(AccountTests.class.getName());
	
	@Test
	@Disabled("Was used for setup")
	public void createAccountWithCharacters() {

	    logger.info("STARTED FUNCTION");
	    var player = new Account();
	    player.username = "donkey kong";
	    player.password = "bluejay";
	    player.role = "player";
	    accountRepository.save(player);
	    
	    var arthur = new Character();
	    var merlin = new Character();
	    var viviane = new Character();
	    arthur.name = "arthur";
	    merlin.name = "merlin";
	    viviane.name = "viviane";
	    List.of(arthur,merlin,viviane).forEach(c -> c.account = player);
	    accountRepository.save(player);
	    player.characters.addAll(List.of(arthur,merlin,viviane));

	    characterRepository.save(arthur);
	    characterRepository.save(merlin);
	    characterRepository.save(viviane);

	    logger.info("FINISHED FUNCTION");
	}


	@Test
	@Disabled("Not concerned with this test right now")
	void createAccount() {


	    var admin = new Account();
	    Assertions.assertAll("account",
		() -> Assertions.assertNull(admin.id),
		() -> Assertions.assertNull(admin.username),
		() -> Assertions.assertNull(admin.password)
	    );

	    admin.username = "morgan_admin";
	    admin.password = "bluejay_admin";
	    admin.role = "admin";
	    
	    var player = new Account();
	    player.username = "morgan";
	    player.password = "bluejay";
	    player.role = "player";

	    var player2 = new Account();
	    player2.username = "donkey_kong";
	    player2.password = "king_kong";
	    player2.role = "player";


	    accountRepository.save(admin);
	    accountRepository.save(player);
	    accountRepository.save(player2);

	    var p = accountRepository.findByUsername("morgan_admin");
	    Assertions.assertTrue(p.username == "morgan_admin");
	    Assertions.assertTrue(p.password == "bluejay_admin");
	    Assertions.assertTrue(p.role == "admin");

	}

	@Test
	@Transactional
	@Disabled
	void accessCharacters() {

	    List<Account> players = accountRepository.findAllByRole("player");
	    players.forEach(p -> {
		    p.characters.forEach(c -> logger.info(c.toString()));
	    });
	    logger.info("Finished");
	}

	@Test
	@Transactional
	@Disabled
	void accessCharacterIds() {
	    List<Account> players = accountRepository.findAllByRole("player");
	    players.forEach(p -> {
		p.characters.forEach(c -> logger.info(c.id.toString()));
	    });
	}

	@Test
	@Transactional
	void accessCharacterSize() {
	    List<Account> players = accountRepository.findAllByRole("player");
	    players.forEach(p -> logger.info(String.format("Size: %d", p.characters.size())));
	}

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

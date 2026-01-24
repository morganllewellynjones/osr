package com.osr.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
//import org.springframework.boot.test.context.SpringBootTest;
import com.osr.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.logging.Logger;
import java.util.logging.Level;


@DataJpaTest
class AccountTests {

	@Autowired
	private AccountRepository accountRepository;

	private static final Logger logger = Logger.getLogger(AccountTests.class.getName());

	@Test
	void createAccount() {

	    var account = new Account();
	    Assertions.assertAll("profile",
		() -> Assertions.assertNull(account.id),
		() -> Assertions.assertNull(account.username),
		() -> Assertions.assertNull(account.password)
	    );

	    account.username = "morgan_admin";
	    account.password = "bluejay_admin";
	    account.role = "admin";

	    accountRepository.save(account);

	    var profiles = accountRepository.findAll();
	    for (Account p : profiles) {
		Assertions.assertTrue(p.username == "morgan_admin");
		Assertions.assertTrue(p.password == "bluejay_admin");
		Assertions.assertTrue(p.role == "admin");
	    }

	}

}

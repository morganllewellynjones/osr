package com.osr.account;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountRepository accountRepository;

    AccountController(AccountRepository accountRepository) {
	this.accountRepository = accountRepository;
    }

    @GetMapping("/findAllProfiles")
    public ResponseEntity<List<Account>> findAllAccount() {
	return ResponseEntity.ok().body(accountRepository.findAll());
    }
    
    @PostMapping("/create")
    void create() {}

    @PatchMapping("/update")
    void update() {}

    @DeleteMapping("/delete")
    void delete() {}
}

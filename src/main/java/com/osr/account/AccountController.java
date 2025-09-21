package com.osr.account;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/account")
public class AccountController {
    
    @PostMapping("/login")
    String login() {
	return "login";
    }

    @PostMapping("/create")
    void create() {}

    @PatchMapping("/update")
    void update() {}

    @DeleteMapping("/delete")
    void delete() {}
}

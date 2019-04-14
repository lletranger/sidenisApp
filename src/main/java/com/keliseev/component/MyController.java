package com.keliseev.component;

import com.keliseev.to.AccountTO;
import com.keliseev.entity.Vault;
import com.keliseev.exception.MyException;
import com.keliseev.to.SendRequestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = "/")
@Validated
public class MyController {

    private Vault vault;

    @Autowired
    public MyController(Vault vault) {
        this.vault = vault;
    }

    @GetMapping(value = "/accounts")
    @ResponseBody
    public ResponseEntity getAllAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(vault.getAllAccounts());
    }

    @GetMapping(value = "/account/{id}")
    @ResponseBody
    public ResponseEntity getAccountInfo(@PathVariable @NotBlank Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(vault.getAccountInfo(id));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    @PostMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity createAccount(@RequestBody AccountTO newAccount) {
        return ResponseEntity.status(HttpStatus.OK).body(vault.createAccount(newAccount));
    }

    @DeleteMapping(value = "/account/{id}")
    @ResponseBody
    public ResponseEntity closeAccount(@PathVariable @NotBlank Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(vault.closeAccount(id));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    @PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity transfer(@RequestBody SendRequestTO requestTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(vault.transfer(requestTO.getFrom(), requestTO.getTo(), requestTO.getAmount()));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }
}
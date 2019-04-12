package com.keliseev.component;

import com.keliseev.entity.Vault;
import com.keliseev.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@RequestMapping(value = "/")
@Validated
public class MyController {

    private Vault vault;

    @Autowired
    public MyController(Vault vault) {
        this.vault = vault;
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAllAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(vault.getAllAccounts());
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAccountInfo(@PathVariable @NotBlank Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(vault.getAccountInfo(id));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/account/{name}:{amount}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity createAccount(@PathVariable @NotBlank @Size(max = 25) String name,
                                        @PathVariable @NotBlank @Min(0) Integer amount) {
        return ResponseEntity.status(HttpStatus.OK).body(vault.createAccount(name, amount));
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity closeAccount(@PathVariable @NotBlank Long id) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(vault.closeAccount(id));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/transfer/{from}:{to}:{amount}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity transfer(@PathVariable @NotBlank Long from,
                                   @PathVariable @NotBlank Long to,
                                   @PathVariable @NotBlank @Min(0) Integer amount) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(vault.transfer(from, to, amount));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }
}
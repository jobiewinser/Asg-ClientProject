package com.nsa.team10.asgproject.controllers.api.v1;

import com.nsa.team10.asgproject.config.DefaultUserDetails;
import com.nsa.team10.asgproject.repositories.daos.UserDao;
import com.nsa.team10.asgproject.services.dtos.NewUserDto;
import com.nsa.team10.asgproject.services.interfaces.IAccountService;
import com.nsa.team10.asgproject.validation.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
public class AccountApiController
{
    private final IAccountService accountService;

    @Autowired
    public AccountApiController(IAccountService accountService)
    {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity create(@Valid @RequestBody NewUserDto newUser)
    {
        try
        {
            accountService.register(newUser);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        catch (ConflictException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/loggedin")
    public ResponseEntity<UserDao> getLoggedInUser()
    {
        var userDetails = getCurrentUserDetails();
        if (userDetails.isPresent())
            return new ResponseEntity<>(userDetails.get().getUser(), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }

    private Optional<DefaultUserDetails> getCurrentUserDetails()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return Optional.of((DefaultUserDetails) authentication.getPrincipal());
        else return Optional.empty();
    }
}

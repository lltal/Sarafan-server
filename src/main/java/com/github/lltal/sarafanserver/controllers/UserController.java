package com.github.lltal.sarafanserver.controllers;

import com.github.lltal.sarafanserver.annotations.CurrentUser;
import com.github.lltal.sarafanserver.domain.User;
import com.github.lltal.sarafanserver.exceptions.ResourceNotFoundException;
import com.github.lltal.sarafanserver.repo.UserRepo;
import com.github.lltal.sarafanserver.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/me")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepo.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @GetMapping
    public Iterable<User> getAllUsers(){
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") User user){
        return user;
    }

}

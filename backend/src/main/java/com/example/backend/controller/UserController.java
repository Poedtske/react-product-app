package com.example.backend.controller;

import com.example.backend.config.UserAuthProvider;
import com.example.backend.dto.CartDto;
import com.example.backend.dto.CredentialsDto;
import com.example.backend.dto.SignUpDto;
import com.example.backend.dto.UserDto;
import com.example.backend.model.LoginRequest;
import com.example.backend.model.User;
import com.example.backend.service.AuthService;
import com.example.backend.service.impl.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/secure")
public class UserController {

    private final UserService userService;
    private final AuthService service;



    @GetMapping("/profile")
    public UserDto getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the logged-in username
        return userService.getUserProfile(username); // Use the service to fetch user details
    }

    @GetMapping("/cart")
    public CartDto getCart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the logged-in username
        return userService.getCart(username); // Use the service to fetch user details
    }



    /*@GetMapping("/users")
    public Iterable<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id){
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody()User user, @PathVariable("id")Long id){
        return userService.updateUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }*/

    /*@PostMapping("/register")
    public String registerUser(@ModelAttribute("account") UserDto userDto, Model model) {
        try {
            userService.registerUser(userDto);
            return "redirect:/login?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }*/

}


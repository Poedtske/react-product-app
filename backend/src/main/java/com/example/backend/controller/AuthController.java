package com.example.backend.controller;

import com.example.backend.model.LoginRequest;
import com.example.backend.model.User;
import com.example.backend.service.impl.UserDto;
import com.example.backend.service.impl.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /*@GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", new UserDto());
        return "register";
    }*/

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody() User user) {
        User newUser=userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session){
        try{
            boolean isAuthenticated= userService.authenticate(loginRequest.getEmail(),loginRequest.getPassword());

            if(isAuthenticated){
                session.setAttribute("user",loginRequest.getEmail());
                return ResponseEntity.ok("lol");
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unknown error occurred");
        }
    }

    @GetMapping("/users")
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
    }

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


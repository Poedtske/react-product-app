package com.example.backend.service.impl;

import com.example.backend.dto.CredentialsDto;
import com.example.backend.dto.SignUpDto;
import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.AppException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;



    /*public void registerUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered!");
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(Role.USER));

        userRepository.save(user);
    }

    public User addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public boolean authenticate(String email, String password){
        User user= userRepository.findByEmail(email);

        if(!user.getEmail().equals(email)){
            throw new UsernameNotFoundException("User does not exist in the database");
        }

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("The password is incorrect");
        }

        return true;
    }*/

    public UserDto findByEmail(String email){
        User user=userRepository.findByEmail(email).orElseThrow(()->new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public UserDto login(CredentialsDto credentialsDto){
        User user=userRepository.findByEmail(credentialsDto.getEmail()).orElseThrow(()->new AppException("Unknown user",HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())){
            return userMapper.toUserDto(user);
        }else{
            throw new AppException("The password is incorrect",HttpStatus.BAD_REQUEST);
        }
    }

    public UserDto register(SignUpDto userDto){
        Optional<User> userOptional= userRepository.findByEmail(userDto.getEmail());

        if(userOptional.isPresent()){
            throw new AppException("User with this email already exists",HttpStatus.BAD_REQUEST);
        }

        User user=userMapper.signUpToUser(userDto);

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));

        User savedUser=userRepository.save(user);
        return userMapper.toUserDto(user);
    }

}


package com.example.backend.service.impl;

import com.example.backend.dto.CartDto;
import com.example.backend.dto.CredentialsDto;
import com.example.backend.dto.SignUpDto;
import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.AppException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.Invoice;
import com.example.backend.model.Product;
import com.example.backend.model.Ticket;
import com.example.backend.model.User;
import com.example.backend.repository.InvoiceRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProductServiceImpl productService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TicketServiceImpl ticketService;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;



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

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new AppException("Unknown user", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity addProductToUserCart(String email, Product p){
        try{
            User user=userRepository.findByEmail(email).orElseThrow(()->new AppException("Unknown user", HttpStatus.NOT_FOUND));
            Invoice i = user.getActiveInvoice();
            i.addProduct(p);
            productService.addInvoiceToProduct(i,p);

            invoiceRepository.save(i);
            return ResponseEntity.ok().build();
        }catch (AppException a){
            return ResponseEntity.badRequest().body(a.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
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

    public UserDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map User to UserDto
        return new UserDto(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public CartDto getCart(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Invoice i = user.getActiveInvoice();
        invoiceRepository.save(i);
        return new CartDto(i.getProducts(),i.getTickets());
        // Map User to CartDto

    }

    @Transactional
    public ResponseEntity clearCart(String email) {
        try{
            User u=this.findUserByEmail(email);
            Invoice i =u.getActiveInvoice();
            i.getProducts().clear();

            i.getTickets().clear();
            invoiceRepository.save(i);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity removeTicket(String email, Long id) {
        try{
            User u=this.findUserByEmail(email);
            Invoice i = u.getActiveInvoice();
            i.removeTicket(ticketService.findTicketById(id));
            invoiceRepository.save(i);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity removeProduct(String email, Long id) {
        try{
            User u=this.findUserByEmail(email);
            Invoice i = u.getActiveInvoice();
            i.removeProduct(productRepository.findById(id).orElseThrow());
            invoiceRepository.save(i);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}


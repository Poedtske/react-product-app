package com.example.backend.model;

import com.example.backend.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.*;

@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    @Column(unique = true) // Ensure email is unique in the database
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // Ensure a role is always set
    private Role role = Role.USER; // Default role

    @ManyToMany
    @JoinTable(
            name = "user_products",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets = new HashSet<>();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public Iterable getProducts(){
        return products;
    }

    // Add a ticket to the set
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setOwner(this);  // Set the owner of the ticket
    }

    // Remove a ticket from the set
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setOwner(null);  // Set the owner to null (or let it be managed by the cascade)
    }

    // Get all tickets for the user
    public Iterable<Ticket> getTickets() {
        return tickets;
    }
}

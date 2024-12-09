package com.example.backend.dto;

import com.example.backend.model.Product;
import com.example.backend.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Map<Product, Integer> productMap;
    private Set<Ticket> tickets;

    public CartDto(Iterable<Product> productList, Iterable<Ticket> ticketSet) {
        this.tickets = new HashSet<>();
        productMap = new HashMap<>();
        productList.forEach(product ->
                productMap.merge(product, 1, Integer::sum)
        );

        ticketSet.forEach(ticket -> {
            tickets.add(ticket);
        });
    }


}

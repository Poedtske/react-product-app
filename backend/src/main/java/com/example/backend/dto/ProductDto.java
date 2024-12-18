package com.example.backend.dto;

import com.example.backend.enums.Category;
import com.example.backend.model.Invoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String img;
    private Boolean available;
    private int quantity;
    private Category category;
    private List<Invoice> invoices;

}

package com.example.backend.controller;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.ProductDto;
import com.example.backend.enums.Category;
import com.example.backend.enums.EventType;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.service.ProductService;
import com.example.backend.service.impl.ProductServiceImpl;
import com.example.backend.service.impl.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private UserService userService;

    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    @PostMapping(value = "/admin/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createProduct(@RequestPart ProductDto productDto, @RequestPart MultipartFile imageFile){
        return productService.createProduct(productDto,imageFile);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestPart Product product,@RequestPart MultipartFile imageFile) throws IOException {
        return productService.updateById(id, product, imageFile);
    }

    @GetMapping("/public/products")
    public List<ProductDto> findAll() {
        return productService.getAllProductDtos();
    }

    @GetMapping("public/products/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("admin/products/{id}")
    public ResponseEntity adminFindById(@PathVariable Long id) {
        return productService.adminFindById(id);
    }

    @DeleteMapping("admin/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        productService.deleteById(id);
    }

    @PostMapping("/secure/products")
    public ResponseEntity addProductToCart(@RequestBody ProductDto productDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the logged-in username
        return userService.addProductToUserCart(username,productDto);
    }

    @GetMapping("/public/categories")
    public List<String> getCategories() {
        return Arrays.stream(Category.values())
                .map(Enum::name)
                .toList();
    }

    @GetMapping("/public/products/{productId}/image")
    public ResponseEntity<?> getImageByProductId(@PathVariable long productId){
        return productService.getImg(productId);
    }

}

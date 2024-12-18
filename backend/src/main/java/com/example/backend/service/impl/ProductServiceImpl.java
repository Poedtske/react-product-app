package com.example.backend.service.impl;

import com.example.backend.dto.ProductDto;
import com.example.backend.model.Invoice;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateById(Long id, Product product, MultipartFile imageFile) throws IOException {

        Product managedProduct = this.findById(id);
        managedProduct.setName(product.getName());
        managedProduct.setQuantity(product.getQuantity());
        managedProduct.setPrice(product.getPrice());
        managedProduct.setImg(imageFile.getName());
        managedProduct.setAvailable(product.getAvailable());
        managedProduct.setCategory(product.getCategory());

        return this.save(managedProduct);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public void addInvoiceToProduct(Invoice i, Product p){
        p.addInvoice(i);
        productRepository.save(p);
    }

    public ResponseEntity createProduct(ProductDto productDto, MultipartFile imageFile){
        try{
            String a=System.getProperty("user.dir");
            String folder="/images/";
            byte[] bytes= imageFile.getBytes();
            Path path=Paths.get(folder+imageFile.getOriginalFilename());
            Files.write(path,bytes);
            Product p=new Product(productDto.getName(),
                    productDto.getPrice(),
                    path.toString(),
                    productDto.getAvailable(),
                    productDto.getQuantity(),
                    productDto.getCategory());
            productRepository.save(p);
            return new ResponseEntity<>(p, HttpStatus.CREATED);

        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}

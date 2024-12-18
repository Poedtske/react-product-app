package com.example.backend.service.impl;

import com.example.backend.dto.ProductDto;
import com.example.backend.exceptions.AppException;
import com.example.backend.model.Invoice;
import com.example.backend.model.Product;
import com.example.backend.repository.ProductDao;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static String IMG_DIR=System.getProperty("user.dir") + "/backend/src/main/resources/images/products";
    private static Path IMGS_PATH=Paths.get(IMG_DIR);

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
    public ResponseEntity deleteById(Long id) {
        try{
            Product p=productRepository.findById(id).orElseThrow();

            if (!Files.exists(IMGS_PATH)) {
                return ResponseEntity.internalServerError().body(null);
            }

            Path filePath = IMGS_PATH.resolve(p.getImg());
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Files.deleteIfExists(filePath);
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public void addInvoiceToProduct(Invoice i, Product p){
        p.addInvoice(i);
        productRepository.save(p);
    }

    public ResponseEntity createProduct(ProductDto productDto, MultipartFile imageFile){
        try{


            if (!Files.exists(IMGS_PATH)) {
                Files.createDirectories(IMGS_PATH);
            }

            // Save product initially without the image path to generate the ID
            Product product = new Product(
                    productDto.getName(),
                    productDto.getPrice(),
                    "", // Temporarily empty image path
                    productDto.getAvailable(),
                    productDto.getQuantity(),
                    productDto.getCategory()
            );
            product = productRepository.save(product); // Save product to get ID

            // Generate the new filename and construct the file path
            String newFilename = product.getName() + "_" + product.getId() + "." +
                    StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
            Path newFilePath = IMGS_PATH.resolve(newFilename);

            // Write the file with the new name
            Files.write(newFilePath, imageFile.getBytes());

            // Update the product with the relative path to the image
            //String relativePath = "images/" + newFilename;
            product.setImg(newFilename);
            productRepository.save(product); // Save updated product

            return new ResponseEntity<>(product, HttpStatus.CREATED);

        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<Resource> getImg(long id) {
        try {
            // Retrieve the product
            Product p = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

            // Construct the file path

            if (!Files.exists(IMGS_PATH)) {
                return ResponseEntity.internalServerError().body(null);
            }

            Path filePath = IMGS_PATH.resolve(p.getImg());
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Load the file as a Resource
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found or not readable");
            }

            // Determine the content type of the image
            String contentType = Files.probeContentType(filePath);
            if (StringUtils.isEmpty(contentType)) {
                contentType = "application/octet-stream"; // Fallback content type
            }

            // Return the image file as a response
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType)) // Set the correct content type for your images
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }


    }

    public ResponseEntity adminFindById(Long id) {
        try{
            Product p=productRepository.findById(id).orElseThrow(()->new AppException("Product not found",HttpStatus.NOT_FOUND));
            return ResponseEntity.ok(p);
        }catch (AppException a){
            return ResponseEntity.internalServerError().body(a.getMessage());
        }
    }
}

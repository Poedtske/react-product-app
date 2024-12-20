package com.example.backend.service.impl;

import com.example.backend.dto.ProductDto;
import com.example.backend.exceptions.AppException;
import com.example.backend.model.Invoice;
import com.example.backend.model.Product;
import com.example.backend.repository.InvoiceRepository;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static String IMG_DIR=System.getProperty("user.dir") + "/backend/src/main/resources/images/products";
    private static Path IMGS_PATH=Paths.get(IMG_DIR);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public ResponseEntity updateById(Long id, ProductDto productDto, MultipartFile imageFile) {

        try{
            Product managedProduct=productRepository.findById(id).orElseThrow(()->new AppException("Product not found",HttpStatus.NOT_FOUND));

            boolean hasUnclosedPaidInvoice = managedProduct.getInvoices().stream()
                    .anyMatch(invoice -> (invoice.getPaid() == true && invoice.getClosed() == false));

            if (hasUnclosedPaidInvoice){
                return ResponseEntity.internalServerError().body("Cannot be updated, there are invoices that have been paid and are still open");
            }

            managedProduct.setName(productDto.getName());
            managedProduct.setPrice(productDto.getPrice());
            managedProduct.setAvailable(productDto.getAvailable());
            managedProduct.setCategory(productDto.getCategory());



            //delete the current img
            if (!Files.exists(IMGS_PATH)) {
                return ResponseEntity.internalServerError().body(null);
            }

            Path filePath = IMGS_PATH.resolve(managedProduct.getImg());
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Files.deleteIfExists(filePath);

            //save the new img

            // Generate the new filename and construct the file path
            String newFilename = managedProduct.getName() + "_" + managedProduct.getId() + "." +
                    StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
            Path newFilePath = IMGS_PATH.resolve(newFilename);

            // Write the file with the new name
            Files.write(newFilePath, imageFile.getBytes());

            // Update the product with the relative path to the image
            //String relativePath = "images/" + newFilename;
            managedProduct.setImg(newFilename);

            this.save(managedProduct);

            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }


    }

    public ResponseEntity updateById(Long id, ProductDto productDto) {

        try{
            Product managedProduct=productRepository.findById(id).orElseThrow(()->new AppException("Product not found",HttpStatus.NOT_FOUND));

            boolean hasUnclosedPaidInvoice = managedProduct.getInvoices().stream()
                    .anyMatch(invoice -> (invoice.getPaid() == true && invoice.getClosed() == false));

            if (hasUnclosedPaidInvoice){
                return ResponseEntity.internalServerError().body("Cannot be updated, there are invoices that have been paid and are still open");
            }

            managedProduct.setName(productDto.getName());
            managedProduct.setPrice(productDto.getPrice());
            managedProduct.setAvailable(productDto.getAvailable());
            managedProduct.setCategory(productDto.getCategory());

            this.save(managedProduct);

            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }


    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<ProductDto> getAllProductDtos() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImg(),
                        product.getCategory(),
                        product.getAvailable(),
                        product.getInvoices()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Product findById(Long id) {
        Product p= productRepository.findById(id).orElseThrow(()-> new AppException("Product not found",HttpStatus.NOT_FOUND));
        if(p.getAvailable()){
            return p;
        }else{
            return null;
        }
    }

    /*public ResponseEntity<ProductDto> findProductDtoById(Long id){
        Product product= productRepository.findById(id).orElseThrow(()->new AppException("Product not found",HttpStatus.NOT_FOUND));
        ProductDto productDto=new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImg(),
                product.getCategory(),
                product.getAvailable(),
                product.getInvoices());
        return ResponseEntity.ok().body(productDto);
    }*/

    public ResponseEntity<?> getProductDtoById(Long id) {
        try{
            Product p= productRepository.findById(id).orElseThrow(()-> new AppException("Product not found",HttpStatus.NOT_FOUND));
            /*if(p.getAvailable()==false){
                throw  new AppException("Product not found",HttpStatus.NOT_FOUND);
            }*/
            ProductDto productDto=new ProductDto(p.getId(),p.getName(),p.getPrice(),p.getImg(),p.getCategory(),p.getAvailable(),p.getInvoices());
            return ResponseEntity.ok(productDto);
        } catch (AppException a){
            return ResponseEntity.internalServerError().body(a.getMessage());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
        try{
            Product p=productRepository.findById(id).orElseThrow();

            boolean hasUnclosedPaidInvoice = p.getInvoices().stream()
                    .anyMatch(invoice -> (invoice.getPaid() == true && invoice.getClosed() == false));

            if (hasUnclosedPaidInvoice){
                return ResponseEntity.internalServerError().body("Cannot be deleted, there are invoices that have been paid and are still open");
            }

            p.getInvoices().forEach(invoice -> {
                if(invoice.getPaid()==false){
                    invoice.removeProduct(p);
                    invoiceRepository.save(invoice);
                } else if (invoice.getPaid()==true && invoice.getClosed()==true) {
                    invoiceRepository.delete(invoice);
                }
            });
            //p.getInvoices().removeIf(invoice -> invoice.getPaid()==false);
            //p.getInvoices().removeIf(invoice -> invoice.getPaid()==true && invoice.getClosed()==true);

            productRepository.save(p);

            if (!Files.exists(IMGS_PATH)) {
                return ResponseEntity.internalServerError().body(null);
            }

            Path filePath = IMGS_PATH.resolve(p.getImg());
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            productRepository.deleteById(id);
            Files.deleteIfExists(filePath);
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
            ProductDto productDto= new ProductDto(
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getImg(),
                    p.getCategory(),
                    p.getAvailable(),
                    p.getInvoices());
            return ResponseEntity.ok(productDto);
        }catch (AppException a){
            return ResponseEntity.internalServerError().body(a.getMessage());
        }
    }

    public ResponseEntity availabilityProduct(Long id){
        try{
            Product p= productRepository.findById(id).orElseThrow(()->new AppException("Product not found",HttpStatus.NOT_FOUND));
            if(p.getAvailable()==false){
                p.setAvailable(true);
            }else{
                p.setAvailable(false);
            }
            productRepository.save(p);
            return ResponseEntity.ok().build();
        }catch (AppException a){
            return ResponseEntity.badRequest().body(a.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}

package com.example.backend;

import com.example.backend.enums.Category;
import com.example.backend.enums.EventType;
import com.example.backend.model.Event;
import com.example.backend.model.Product;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        /*productService.save(new Product("iPhone",BigDecimal.valueOf(1000D),null,true, Category.DRINK,null));
        productService.save(new Product("Blackberry",BigDecimal.valueOf(1100D),null,true, Category.DRINK,null));

        Event event = new Event("Sample Event", "Sample Location", "Sample Description", EventType.CONCERT, "5x5", 4);
        System.out.println("Height: " + event.getKolommen() + ", Length: " + event.getRijen());
        System.out.println("Number of tables created: " + event.getTables().size());*/


    }
}

package com.DailycodeBuffer.spring_security_6.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class productController {

    private record Product(Integer proid, String proname, double price){}

    List<Product> products=new ArrayList<>(List.of(
            new Product(1,"car",150000.0),
            new Product(2,"truck",100000.0)
    ));

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        return ResponseEntity.ok()
                .body(products);
    }

    @PostMapping
    public ResponseEntity<Product> saveproduct(@RequestBody Product product){
        try{
            products.add(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(product);
    }
}

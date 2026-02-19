package com.example.springangular.controller;

import com.example.springangular.model.Product;
import com.example.springangular.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts() {
        return productService.findAll().toString();
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(Product::toString)
                .orElse("Product not found with id: " + id);
    }

    @PostMapping
    public String createProduct(@RequestBody Product product) {
        Product createdProduct = productService.create(product);
        return "Created product: " + createdProduct;
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product)
                .map(updated -> "Updated product: " + updated)
                .orElse("Product not found with id: " + id);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        if (productService.delete(id)) {
            return "Deleted product with id: " + id;
        }
        return "Product not found with id: " + id;
    }
}

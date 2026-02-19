package com.example.springangular.service;

import com.example.springangular.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    public Product create(Product product) {
        Long id = idGenerator.incrementAndGet();
        Product newProduct = new Product(id, product.getName(), product.getDescription(), product.getPrice());
        products.put(id, newProduct);
        return newProduct;
    }

    public Optional<Product> update(Long id, Product product) {
        if (!products.containsKey(id)) {
            return Optional.empty();
        }

        Product updatedProduct = new Product(id, product.getName(), product.getDescription(), product.getPrice());
        products.put(id, updatedProduct);
        return Optional.of(updatedProduct);
    }

    public boolean delete(Long id) {
        return products.remove(id) != null;
    }
}

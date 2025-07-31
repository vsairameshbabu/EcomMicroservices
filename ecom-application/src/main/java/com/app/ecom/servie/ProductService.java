package com.app.ecom.servie;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(productRequest, product);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public Optional<ProductResponse> updateProduct( Long id, ProductRequest productRequest) {
        return productRepository.findById(id).map(existingProduct -> {
            updateProductFromRequest(productRequest, existingProduct);
            Product updatedProduct = productRepository.save(existingProduct);
            return mapToProductResponse(updatedProduct);
        });
    }

    public Optional<ProductResponse> fetchProduct(Long id) {
        return productRepository.findById(id).map(this::mapToProductResponse);
    }

    public List<ProductResponse> fetchAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void updateProductFromRequest(ProductRequest productRequest, Product product) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setImageUrl(productRequest.getImageUrl());
        product.setActive(productRequest.getActive());
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());
        return response;
    }

}

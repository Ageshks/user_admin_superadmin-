package admin_user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import admin_user.model.Product;
import admin_user.repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Method to get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Method to get a product by ID
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    // Method to save a new product
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    // Method to update an existing product
    public void updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        // existingProduct.setDescription(updatedProduct.getDescription()); // Add if description is part of the model
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setPrice(updatedProduct.getPrice());

        productRepository.save(existingProduct);
    }

    // Method to delete a product by ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Method to handle product purchase
    public void purchaseProduct(Long id) {
        Product product = getProductById(id);
        if (product.getQuantity() > 0) {
            product.setQuantity(product.getQuantity() - 1); // Reduce quantity by 1 for each purchase
            productRepository.save(product);
        } else {
            throw new RuntimeException("Product is out of stock!");
        }
    }
}

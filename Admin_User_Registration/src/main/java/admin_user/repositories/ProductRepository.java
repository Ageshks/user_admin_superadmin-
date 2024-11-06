package admin_user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import admin_user.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

package admin_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import admin_user.model.Product;
import admin_user.service.ProductService;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/super-admin/products")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String viewProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "view-products"; // Ensure this view exists
    }

    @GetMapping("/super-admin/products/add")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "add-product"; // Ensure this view exists
    }

    @PostMapping("/super-admin/products/add")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String addProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/super-admin/products"; // Redirect to the products list
    }

    @GetMapping("/super-admin/products/edit/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getEditProductPage(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit-product"; // Ensure this view exists
    }

    @PostMapping("/super-admin/products/edit/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("product") Product updatedProduct) {
        productService.updateProduct(id, updatedProduct);
        return "redirect:/super-admin/products"; // Redirect to the products list
    }

    @GetMapping("/super-admin/products/delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/super-admin/products"; // Redirect to the products list
    }


    @GetMapping("/user/products")
    public String showProductsForUser(Model model) {
    model.addAttribute("products", productService.getAllProducts());
    return "user-products"; // Thymeleaf template for viewing products by users
    }

    @PostMapping("/user/purchase/{id}")
    public String purchaseProduct(@PathVariable("id") Long id) {
    // Implement logic to handle product purchase, e.g., updating quantity, order record, etc.
    productService.purchaseProduct(id);
    return "redirect:/user/products";
}



}

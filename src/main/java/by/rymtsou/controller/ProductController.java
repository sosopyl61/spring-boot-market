package by.rymtsou.controller;

import by.rymtsou.model.Product;
import by.rymtsou.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all-products")
    public String getAllProducts(Model model, HttpServletResponse response) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/create")
    public String getCreateProductPage() {
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute("product") @Valid Product product,
                                Model model, HttpServletResponse response) {
        Optional<Product> createdProduct = productService.createProduct(product);
        if (createdProduct.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("message", "Product creation failed.");
            return "innerError";
        }

        model.addAttribute("product", createdProduct.get());
        return "redirect:/products/all-products";
    }

    @GetMapping("/update/{id}")
    public String getProductUpdatePage(@PathVariable("id") Long id, Model model, HttpServletResponse response) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("message", "Product not found: id=" + id);
            return "innerError";
        }
        model.addAttribute("product", product.get());
        return "editProduct";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") @Valid Product product,
                                Model model, HttpServletResponse response) {
        Optional<Product> productUpdated = productService.updateProduct(product);
        if (productUpdated.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("message", "Product update failed.");
            return "innerError";
        }
        model.addAttribute("product", productUpdated.get());
        return "redirect:/products/all-products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model, HttpServletResponse response) {
        Optional<Product> deletedProduct  = productService.deleteProduct(id);
        if (deletedProduct.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("message", "Product deletion failed.");
            return "innerError";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return "redirect:/products/all-products";
    }
}

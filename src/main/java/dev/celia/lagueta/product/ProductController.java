package dev.celia.lagueta.product;

import dev.celia.lagueta.image.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;

    public ProductController(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @PostMapping("/with-image")
    public ResponseEntity<Product> createProductWithImage(
            @RequestPart("product") Product product,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            if (file != null && !file.isEmpty()) {
                String fileName = imageService.saveImage(file, product);
                product.setImage(fileName);
            }
            Product saved = productService.save(product);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/with-image/{id}")
    public ResponseEntity<Product> updateProductWithImage(
            @PathVariable Long id,
            @RequestPart("product") Product updatedProduct,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            return productService.findById(id)
                    .map(existingProduct -> {
                        existingProduct.setName(updatedProduct.getName());
                        existingProduct.setDescription(updatedProduct.getDescription());
                        existingProduct.setCategory(updatedProduct.getCategory());

                        if (file != null && !file.isEmpty()) {
                            try {
                                String fileName = imageService.saveImage(file, existingProduct);
                                existingProduct.setImage(fileName);
                            } catch (IOException e) {
                                throw new RuntimeException("Error saving image", e);
                            }
                        }
                        Product saved = productService.save(existingProduct);
                        return ResponseEntity.ok(saved);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.findById(id).isPresent()) {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

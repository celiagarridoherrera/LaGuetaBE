package dev.celia.lagueta.product;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import dev.celia.lagueta.image.ImageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
            @RequestPart("file") MultipartFile file
    ) {
        try {
            String fileName = imageService.saveImage(file, product);
            product.setImage(fileName);
            Product saved = productService.save(product);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updateproduct) {
        return productService.findById(id)
                .map(product -> {
                    product.setName(updateproduct.getName());
                    product.setDescription(updateproduct.getDescription());
                    product.setCategory(updateproduct.getCategory());
                    product.setImage(updateproduct.getImage());
                    return ResponseEntity.ok(productService.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
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

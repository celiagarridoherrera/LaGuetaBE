package dev.celia.lagueta.image;

import dev.celia.lagueta.product.Product;
import dev.celia.lagueta.product.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;
    private final ProductRepository productRepository;

    public ImageController(ImageService imageService, ProductRepository productRepository) {
        this.imageService = imageService;
        this.productRepository = productRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam(name = "productId", required = false) Long productId) {
        try {
            Product product = null;
            if (productId != null) {
                product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            }

            String imageUrl = imageService.saveImage(file, product);
            return ResponseEntity.ok("¡Imagen subida!" + imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la imagen");
        }
    }

    @GetMapping
    public ResponseEntity<List<ImageInfo>> getAllImages() {
        return ResponseEntity.ok(imageService.listImages());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.ok("Imagen eliminada con éxito");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la imagen");
        }
    }
}
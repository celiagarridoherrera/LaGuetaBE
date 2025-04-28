package dev.celia.lagueta.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import dev.celia.lagueta.image.ImageService;

public class ProductControllerTest {

    private ProductService productService;
    private ImageService imageService;
    private ProductController productController;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        imageService = mock(ImageService.class);
        productController = new ProductController(productService, imageService);

        sampleProduct = new Product(1L, "Sidra", "Sidra Llagar de Fozana", "Sidra", "Sidra.jpg");
    }
    
    @Test
    void testCreateProduct() {
        when(productService.save(sampleProduct)).thenReturn(sampleProduct);

        ResponseEntity<Product> response = productController.createProduct(sampleProduct);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Sidra", response.getBody().getName());
        verify(productService).save(sampleProduct);
    }

    @Test
    void testCreateProductWithImageSuccess() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(imageService.saveImage(mockFile, sampleProduct)).thenReturn("Sidra.jpg");
        when(productService.save(sampleProduct)).thenReturn(sampleProduct);

        ResponseEntity<Product> response = productController.createProductWithImage(sampleProduct, mockFile);

        assertEquals(200, response.getStatusCode().value());
        verify(imageService).saveImage(mockFile, sampleProduct);
        verify(productService).save(sampleProduct);
    }

    @Test
    void testCreateProductWithImageError() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(imageService.saveImage(mockFile, sampleProduct)).thenThrow(new IOException());
        when(productService.save(sampleProduct)).thenReturn(sampleProduct);

        ResponseEntity<Product> response = productController.createProductWithImage(sampleProduct, mockFile);

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void testDeleteProductFound() {
        when(productService.findById(1L)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productService).delete(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(productService).delete(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productService.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = productController.deleteProduct(2L);

        assertEquals(404, response.getStatusCode().value());
        verify(productService, never()).delete(any());
    }

    @Test
    void testGetAllProducts() {
        when(productService.findAll()).thenReturn(List.of(sampleProduct));

        List<Product> result = productController.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Sidra", result.get(0).getName());
    }

    @Test
    void testGetProductByIdFound() {
        when(productService.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Sidra", response.getBody().getName());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productService.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(2L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateProductWithImageFound() {
        Product updatedProduct = new Product(null, "Sidra", "Sidra Llagar de Fozana", "Sidra", "SidraUpdated.jpg");
        
        when(productService.findById(1L)).thenReturn(Optional.of(sampleProduct));
        
        // Actualiza las propiedades del producto existente
        sampleProduct.setName(updatedProduct.getName());
        sampleProduct.setDescription(updatedProduct.getDescription());
        sampleProduct.setCategory(updatedProduct.getCategory());
        sampleProduct.setImage(updatedProduct.getImage());

        when(productService.save(sampleProduct)).thenReturn(sampleProduct);

        ResponseEntity<Product> response = productController.updateProductWithImage(1L, updatedProduct, null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Sidra", response.getBody().getName());
        assertEquals("SidraUpdated.jpg", response.getBody().getImage());
    }

    @Test
    void testUpdateProductWithImageNotFound() {
        Product updatedProduct = new Product(null, "Sidra", "Sidra Llagar de Fozana", "Sidra", "SidraUpdated.jpg");
        
        when(productService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.updateProductWithImage(99L, updatedProduct, null);

        assertEquals(404, response.getStatusCode().value());
    }
}

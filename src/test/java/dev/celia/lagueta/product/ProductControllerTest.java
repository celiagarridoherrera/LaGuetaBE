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
    void testUpdateProductFound() {
        Product updatedProduct = new Product(null, "Sidra", "Sidra Llagar de Fozana", "Sidra", "Sidra.jpg");
        
        when(productService.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productService.save(sampleProduct)).thenReturn(updatedProduct);

        ResponseEntity<Product> response = productController.updateProduct(1L, updatedProduct);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Sidra", response.getBody().getName());
    }

    @Test
    void testUpdateProductNotFound() {
        Product updatedProduct = new Product(null, "Sidra", "Sidra Llagar de Fozana", "Sidra", "Sidra.jpg");
        
        when(productService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.updateProduct(99L, updatedProduct);

        assertEquals(404, response.getStatusCode().value());
    }
}

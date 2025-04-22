package dev.celia.lagueta.image;

import dev.celia.lagueta.product.Product;
import dev.celia.lagueta.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ImageControllerTest {

    @InjectMocks
    private ImageController imageController;

    @Mock
    private ImageService imageService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllImages() {
        List<ImageInfo> images = List.of(new ImageInfo("foto.jpg", "/images/foto.jpg"));
        when(imageService.listImages()).thenReturn(images);

        ResponseEntity<List<ImageInfo>> response = imageController.getAllImages();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(imageService).listImages();
    }

    @Test
    void testDeleteImageById() throws IOException {
        doNothing().when(imageService).deleteImage(1L);

        ResponseEntity<String> response = imageController.deleteImage(1L);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains("éxito"));
    }

    @Test
    void testDeleteImageByFilename_Success() {
        when(imageService.deleteImage("foto.jpg")).thenReturn(true);

        ResponseEntity<String> response = imageController.deleteImage("foto.jpg");

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains("éxito"));
    }

    @Test
    void testDeleteImageByFilename_NotFound() {
        when(imageService.deleteImage("foto.jpg")).thenReturn(false);

        ResponseEntity<String> response = imageController.deleteImage("foto.jpg");

        assertEquals(404, response.getStatusCode().value());
        assertTrue(response.getBody().contains("no encontrada"));
    }

    @Test
    void testUploadImageWithProduct() throws IOException {
        Product product = new Product(1L, "Cerveza", "desc", "cat", "foto.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "foto.jpg", "image/jpeg", "contenido".getBytes());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(imageService.saveImage(any(), eq(product))).thenReturn("/images/foto.jpg");

        ResponseEntity<String> response = imageController.uploadImage(file, 1L);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains("¡Imagen subida!"));
    }

    @Test
    void testUploadImageWithoutProduct() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "foto.jpg", "image/jpeg", "contenido".getBytes());

        when(imageService.saveImage(any(), isNull())).thenReturn("/images/foto.jpg");

        ResponseEntity<String> response = imageController.uploadImage(file, null);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains("¡Imagen subida!"));
    }
}

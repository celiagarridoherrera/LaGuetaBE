package dev.celia.lagueta.image;

import dev.celia.lagueta.product.Product;
import dev.celia.lagueta.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        assertTrue(response.getBody().contains("Imagen eliminada con éxito"));
    }

    @Test
    void testDeleteImageByIdError() throws IOException {
        doThrow(new IOException()).when(imageService).deleteImage(1L);

        ResponseEntity<String> response = imageController.deleteImage(1L);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().contains("Error al eliminar la imagen"));
    }

    @Test
    void testDeleteImageByFilename_Success() {

        when(imageService.deleteImage(1L)).thenReturn(true);

        ResponseEntity<String> response = imageController.deleteImage(1L);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains("Imagen eliminada con éxito"));
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
        verify(imageService).saveImage(file, product); 
    }

    @Test
    void testUploadImageWithoutProduct() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "foto.jpg", "image/jpeg", "contenido".getBytes());

        when(imageService.saveImage(file, null)).thenReturn("/images/foto.jpg");

        ResponseEntity<String> response = imageController.uploadImage(file, null);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains("¡Imagen subida!"));
        verify(imageService).saveImage(file, null); 
    }

    @Test
    void testUploadImageError() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "foto.jpg", "image/jpeg", "contenido".getBytes());

        when(imageService.saveImage(file, null)).thenThrow(new IOException("Error al subir la imagen"));

        ResponseEntity<String> response = imageController.uploadImage(file, null);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().contains("Error al subir la imagen"));
    }
}

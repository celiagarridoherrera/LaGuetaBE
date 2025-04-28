package dev.celia.lagueta.image;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import dev.celia.lagueta.product.Product;

public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    private final Path uploadDir = Paths.get("uploads");

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        Files.createDirectories(uploadDir); 
    }

    @AfterEach
    void cleanUp() throws IOException {
        Files.walk(uploadDir)
            .filter(Files::isRegularFile)
            .forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    @Test
    void testSaveImage() throws IOException {
        Product product = new Product();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "mock image content".getBytes());

        String url = imageService.saveImage(file, product);

        assertEquals("/images/test.jpg", url);

        assertTrue(Files.exists(uploadDir.resolve("test.jpg")));

        verify(imageRepository, times(1)).save(any(ImageInfo.class));
    }

    @Test
    void testListImages() {
        when(imageRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(imageService.listImages().isEmpty());

        verify(imageRepository, times(1)).findAll();
    }

    @Test
    void testDeleteImageById() throws IOException {
        String filename = "test.jpg";
        Path filePath = uploadDir.resolve(filename);
        
        Files.write(filePath, "dummy".getBytes());

        ImageInfo image = new ImageInfo(filename, "/images/" + filename);
        
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        imageService.deleteImage(1L);

        assertFalse(Files.exists(filePath));

        verify(imageRepository, times(1)).delete(image);
    }

    @Test
    void testDeleteImageByFilename() throws IOException {
        String filename = "delete_me.jpg";
        Path filePath = uploadDir.resolve(filename);
        
        Files.write(filePath, "delete this file".getBytes());

        boolean deleted = imageService.deleteImage(filename);


        assertTrue(deleted);
        assertFalse(Files.exists(filePath));  
    }

    @Test
    void testSaveImageEmptyFile() {
        Product product = new Product();
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[0]);

        assertThrows(IOException.class, () -> imageService.saveImage(emptyFile, product));
    }
}

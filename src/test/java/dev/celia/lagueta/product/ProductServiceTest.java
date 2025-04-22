package dev.celia.lagueta.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product(1L, "Sidra", "Sidra natural asturiana", "Bebida", "imagen.png");
    }

    @Test
    void testSave() {
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        Product savedProduct = productService.save(sampleProduct);

        assertNotNull(savedProduct);
        assertEquals("Sidra", savedProduct.getName());
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    void testFindAll() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));

        List<Product> result = productService.findAll();

        assertEquals(1, result.size());
        assertEquals("Sidra", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Optional<Product> result = productService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Sidra", result.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete() {
        doNothing().when(productRepository).deleteById(1L);

        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}

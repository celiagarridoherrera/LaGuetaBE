package dev.celia.lagueta.image;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dev.celia.lagueta.product.Product;

public class ImageInfoTest {
    @Test
    void testSetandGetId() {
        ImageInfo image = new ImageInfo();
        image.setId(1L);
        assertEquals(1L, image.getId());
    }

    @Test
    void testSetandGetName() {
        ImageInfo image = new ImageInfo();
        image.setName("foto.jpg");
        assertEquals("foto.jpg", image.getName());
    }

    @Test
    void testSetandGetUrl() {
        ImageInfo image = new ImageInfo();
        image.setUrl("/images/foto.jpg");
        assertEquals("/images/foto.jpg", image.getUrl());
    }

    @Test
    void testSetandGetProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Sidra");

        ImageInfo image = new ImageInfo();
        image.setProduct(product);

        assertNotNull(product);
        assertEquals(1L, image.getProduct().getId());
        assertEquals("Sidra", image.getProduct().getName());
    }

    @Test
    void testConstructorWithParams() {
        ImageInfo image = new ImageInfo("foto.jpg", "/images/foto.jpg");
        assertEquals("foto.jpg", image.getName());
        assertEquals("/images/foto.jpg", image.getUrl());
    }
}

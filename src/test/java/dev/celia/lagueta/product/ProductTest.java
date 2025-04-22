package dev.celia.lagueta.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ProductTest {
    @Test
    void testGettersandSetters() {
        Product product = new Product();

        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("description test");
        product.setCategory("category test");
        product.setImage("imagetest.jpg");

        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("description test", product.getDescription());
        assertEquals("category test", product.getCategory());
        assertEquals("imagetest.jpg", product.getImage());

    }

    @Test
    void testConstructor() {
        Product product = new Product(1L, "Test Product", "description test", "category test", "imagetest.jpg");

        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("description test", product.getDescription());
        assertEquals("category test", product.getCategory());
        assertEquals("imagetest.jpg", product.getImage());
    }
}

package dev.celia.lagueta.product;

import java.util.ArrayList;
import java.util.List;

import dev.celia.lagueta.image.ImageInfo;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String category;
    private String image;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ImageInfo> images = new ArrayList<>();

    public Product(){}

    public Product(Long id, String name, String description, String category, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

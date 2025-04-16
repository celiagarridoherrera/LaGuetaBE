package dev.celia.lagueta.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.celia.lagueta.product.Product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageService {

    private final String uploadDir = "uploads";

    @Autowired
    private ImageRepository imageRepository;

    public ImageService() {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String saveImage(MultipartFile file, Product product) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, file.getOriginalFilename());

        Files.write(filePath, file.getBytes());

        String url = "/images/" + fileName;

        ImageInfo image = new ImageInfo(fileName, url);
        image.setProduct(product);
        imageRepository.save(image);

        return url;
    }

    public List<ImageInfo> listImages() {
        return imageRepository.findAll();
    }
    
    public void deleteImage(Long id) throws IOException {
        ImageInfo image = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));

        Path filePath = Paths.get(uploadDir, image.getName());
        Files.deleteIfExists(filePath);
        imageRepository.delete(image);
    }
}

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
        if (file.isEmpty()) {
            throw new IOException("El archivo está vacío");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

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
        ImageInfo image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Eliminar el archivo físico
        Path filePath = Paths.get(uploadDir, image.getName());
        Files.deleteIfExists(filePath);

        imageRepository.delete(image);
    }

    public boolean deleteImage(String filename) {
        Path filePath = Paths.get(uploadDir, filename);
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

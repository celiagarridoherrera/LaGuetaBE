package dev.celia.lagueta.image;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final String uploadDir = "uploads";

    public ImageService() {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String saveImage(MultipartFile file) throws IOException {
        Path filePath = Paths.get(uploadDir, file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
        return file.getOriginalFilename();
    }

    public List<ImageInfo> listImages() {
        File folder = new File(uploadDir);
        File[] files = folder.listFiles();
        if (files == null) return new ArrayList<>();

        return List.of(files).stream()
                .filter(File::isFile)
                .map(file -> new ImageInfo(file.getName(), "/api/images/view/" + file.getName()))
                .collect(Collectors.toList());
    }

    public Resource loadImageAsResource(String filename) throws MalformedURLException {
        Path path = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("No se puede leer el archivo: " + filename);
        }
    }
}

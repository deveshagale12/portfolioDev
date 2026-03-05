package com.portfolio_dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping("/upload")
public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
    PortfolioImage img = new PortfolioImage();
    img.setFileName(file.getOriginalFilename());
    img.setData(file.getBytes());
    img.setContentType(file.getContentType());
    
    PortfolioImage saved = imageRepository.save(img);
    
    // Return the ID so the frontend can save it to the User/Project record
    return ResponseEntity.ok(Map.of("id", saved.getId()));
}

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return imageRepository.findById(id)
                .map(img -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(img.getContentType()))
                        .body(img.getData()))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update an existing image in NeonDB
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return imageRepository.findById(id).map(existingImage -> {
            try {
                existingImage.setFileName(file.getOriginalFilename());
                existingImage.setData(file.getBytes());
                existingImage.setContentType(file.getContentType());
                
                imageRepository.save(existingImage);
                return ResponseEntity.ok("Image updated successfully!");
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error reading file data");
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
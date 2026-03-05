
package com.portfolio_dev;

import com.yourpackage.repository.ProjectRepository; // Change to your actual package
import com.yourpackage.service.FileStorageService;   // Change to your actual package
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
// ... (your existing PostMapping imports)
@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired private FileStorageService storageService;
    @Autowired private ProjectRepository projectRepo;

    @PostMapping("/upload") 
    public ResponseEntity<?> uploadGeneralFile(@RequestParam("file") MultipartFile file) {
        try {
            // Save file using your existing storageService
            String filePath = storageService.save(file);
            
            // Return the URL so the frontend can save it to the User profile
            return ResponseEntity.ok(Map.of(
                "message", "File uploaded successfully", 
                "url", "/api/v1/files/display/" + filePath // path to view the file
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/display/{filename:.+}")
    public ResponseEntity<Resource> displayFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE) // Or detect type
                .body(file);
    }
    @PostMapping("/upload/project/{id}")
    public ResponseEntity<?> uploadProjectImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return projectRepo.findById(id)
            .<ResponseEntity<?>>map(project -> {
                String fileName = storageService.save(file);
                
                // IMPORTANT: Save the path that the browser can actually reach
                String displayUrl = "/api/v1/files/display/" + fileName; 
                project.setImageUrl(displayUrl);
                projectRepo.save(project);
                
                return ResponseEntity.ok(Map.of(
                    "message", "Project image uploaded successfully", 
                    "url", displayUrl
                ));
            })
            .orElse(ResponseEntity.status(404).body(Map.of("error", "Project not found")));
    }
    
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Resource file = storageService.load(filename);
            
            // This header "attachment" forces the browser to download the file 
            // instead of just showing it.
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/delete/project/{id}")
    public ResponseEntity<?> deleteProjectImage(@PathVariable Long id) {
        return projectRepo.findById(id).<ResponseEntity<?>>map(project -> {
            String imageUrl = project.getImageUrl();
            
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // 1. Delete from physical storage
                storageService.delete(imageUrl);
                
                // 2. Clear the URL in the database
                project.setImageUrl(null);
                projectRepo.save(project);
                
                return ResponseEntity.ok(Map.of("message", "File deleted and project updated"));
            }
            
            return ResponseEntity.badRequest().body(Map.of("error", "Project has no image to delete"));
        }).orElse(ResponseEntity.status(404).body(Map.of("error", "Project not found")));
    }
}
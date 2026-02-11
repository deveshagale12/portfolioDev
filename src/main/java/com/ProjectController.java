package com.portfolio_dev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*") // Allows your frontend to call this API
public class ProjectController {

    @Autowired
    private ProjectRepository repository;

    // 1. GET ALL PROJECTS
    @GetMapping
    public List<Project> getAll() {
        return repository.findAll();
    }

    // 2. CREATE PROJECT (POST)
 // Modified POST method to include a "folder" parameter
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Project addProject(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("link") String link,
            @RequestParam(value = "folder", defaultValue = "projects") String folder, 
            @RequestParam("photo") MultipartFile file) throws IOException {
        
        String fileName = saveFile(file, folder); // Uses the subfolder version
        
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setLink(link);
        project.setPhotoPath("uploads/" + folder + "/" + fileName); // Added "uploads/" prefix for easier URL building
        
        return repository.save(project);
    }

    // Unified Helper Method
    private String saveFile(MultipartFile file, String subFolder) throws IOException {
        String uploadDir = "uploads/" + subFolder + "/"; 
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
    
 // Add this to your ProjectController.java if missing
    @GetMapping("/{id}")
    public ResponseEntity<Project> getOne(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // 3. UPDATE PROJECT (PUT)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Project updateProject(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("link") String link,
            @RequestParam(value = "folder", defaultValue = "projects") String folder,
            @RequestParam(value = "photo", required = false) MultipartFile file) throws IOException {

        return repository.findById(id).map(project -> {
            project.setName(name);
            project.setDescription(description);
            project.setLink(link);

            // Only update the photo if a new file is actually uploaded
            if (file != null && !file.isEmpty()) {
                try {
                    String fileName = saveFile(file, folder);
                    project.setPhotoPath(folder + "/" + fileName);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to store new file", e);
                }
            }

            return repository.save(project);
        }).orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    // 4. DELETE PROJECT (DELETE)
    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        repository.deleteById(id);
        return "Project deleted successfully!";
    }

    // --- HELPER METHOD TO SAVE FILE ---
    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads/projects/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
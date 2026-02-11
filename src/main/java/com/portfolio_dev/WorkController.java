package com.portfolio_dev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/work")
@CrossOrigin(origins = "*")
public class WorkController {

    @Autowired
    private WorkRepository repository;

    // 1. POST: Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WorkExperience addWork(
            @RequestParam("nameOfWork") String nameOfWork, // MUST MATCH JS fd.append
            @RequestParam("description") String description,
            @RequestParam("time") String time,
            @RequestParam("photo") MultipartFile file) throws IOException {
        
        String fileName = saveFile(file, "work_images");
        
        WorkExperience work = new WorkExperience();
        work.setNameOfWork(nameOfWork);
        work.setDescription(description);
        work.setTime(time);
        work.setPhotoPath("uploads/work_images/" + fileName);
        
        return repository.save(work);
    }

    // 2. GET: List all
    @GetMapping
    public List<WorkExperience> getAllWork() {
        return repository.findAll();
    }

    // 3. PUT: Update
 // 3. PUT: Update
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WorkExperience> updateWork(
            @PathVariable Long id,
            @RequestParam("nameOfWork") String nameOfWork, // CHANGED from "name"
            @RequestParam("description") String description,
            @RequestParam("time") String time,
            @RequestParam(value = "photo", required = false) MultipartFile file) throws IOException {

        return repository.findById(id).map(work -> {
            work.setNameOfWork(nameOfWork);
            work.setDescription(description);
            work.setTime(time);
            
            if (file != null && !file.isEmpty()) {
                try {
                    String fileName = saveFile(file, "work_images");
                    work.setPhotoPath("uploads/work_images/" + fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return ResponseEntity.ok(repository.save(work));
        }).orElse(ResponseEntity.notFound().build());
    }
    
    private String saveFile(MultipartFile file, String subFolder) throws IOException {
        // 1. Define the path: uploads/work_images/
        Path uploadPath = Paths.get("uploads/" + subFolder);

        // 2. Create the directory if it doesn't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 3. Create a unique file name to avoid overwriting (e.g., 12345_logo.png)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 4. Save the file to the folder
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    // 4. DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWork(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Work history deleted.");
        }
        return ResponseEntity.notFound().build();
    }
    
    

    // Reuse your saveFile method here..
    
 // Add this to WorkController.java
    @GetMapping("/{id}")
    public ResponseEntity<WorkExperience> getWorkById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
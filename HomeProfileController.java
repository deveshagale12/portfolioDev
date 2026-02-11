package com.portfolio_dev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/home-profile")
@CrossOrigin(origins = "*")
public class HomeProfileController {

    @Autowired
    private HomeProfileRepository repository;

    // 1. GET: Fetch the profile
    @GetMapping
    public ResponseEntity<HomeProfile> getProfile() {
        return repository.findAll().stream().findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
 // 5. GET: Fetch specific profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<HomeProfile> getProfileById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // 2. POST: Initial setup
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HomeProfile> createProfile(
            @RequestParam("fullName") String fullName,
            @RequestParam("role") String role,
            @RequestParam("experience") String experience,
            @RequestParam("bio") String bio,
            @RequestParam("githubUrl") String githubUrl,
            @RequestParam("linkedinUrl") String linkedinUrl,
            @RequestParam("twitterUrl") String twitterUrl,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("cv") MultipartFile cv) throws IOException {
        
        HomeProfile profile = new HomeProfile();
        profile.setFullName(fullName);
        profile.setRole(role);
        profile.setExperience(experience);
        profile.setBio(bio);
        profile.setGithubUrl(githubUrl);
        profile.setLinkedinUrl(linkedinUrl);
        profile.setTwitterUrl(twitterUrl);

        // Handle Photo
        String photoName = saveFile(photo, "profile");
        profile.setPhotoPath("uploads/profile/" + photoName);

        // Handle CV (Save in a separate subfolder)
        String cvName = saveFile(cv, "resumes");
        profile.setCvPath("uploads/resumes/" + cvName);
        
        return ResponseEntity.ok(repository.save(profile));
    }

    // 3. PUT: Update everything
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HomeProfile> updateProfile(
            @PathVariable Long id,
            @RequestParam("fullName") String fullName,
            @RequestParam("role") String role,
            @RequestParam("experience") String experience,
            @RequestParam("bio") String bio,
            @RequestParam("githubUrl") String githubUrl,
            @RequestParam("linkedinUrl") String linkedinUrl,
            @RequestParam("twitterUrl") String twitterUrl,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "cv", required = false) MultipartFile cv) throws IOException {

        return repository.findById(id).map(profile -> {
            profile.setFullName(fullName);
            profile.setRole(role);
            profile.setExperience(experience);
            profile.setBio(bio);
            profile.setGithubUrl(githubUrl);
            profile.setLinkedinUrl(linkedinUrl);
            profile.setTwitterUrl(twitterUrl);
            
            try {
                // Update photo only if a new one is provided
                if (photo != null && !photo.isEmpty()) {
                    String photoName = saveFile(photo, "profile");
                    profile.setPhotoPath("uploads/profile/" + photoName);
                }
                // Update CV only if a new one is provided
                if (cv != null && !cv.isEmpty()) {
                    String cvName = saveFile(cv, "resumes");
                    profile.setCvPath("uploads/resumes/" + cvName);
                }
            } catch (IOException e) {
                throw new RuntimeException("File upload failed", e);
            }

            return ResponseEntity.ok(repository.save(profile));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Profile deleted.");
        }
        return ResponseEntity.notFound().build();
    }

    private String saveFile(MultipartFile file, String subFolder) throws IOException {
        Path uploadPath = Paths.get("uploads/" + subFolder);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
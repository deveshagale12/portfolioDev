package com.portfolio_dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
@RestController
@RequestMapping("/api/v1/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private ProjectRepository projectRepo;
    
    @Autowired
    private FileStorageService storageService;

    /**
     * CREATE Profile (Initial Setup)
     * Use this only once to set up your portfolio.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createProfile(@RequestBody User user) {
        // Security check: If a profile already exists, don't create another one
        if (userRepository.count() > 0) {
            return ResponseEntity.badRequest()
                .body("Profile already exists. Use the UPDATE (PUT) method instead.");
        }
        
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * UPDATE Profile
     * Updates your existing data in NeonDB.
     */
    @PutMapping("/update")
public ResponseEntity<?> updateMyPortfolio(@RequestBody User details) {
    return userRepository.findAll().stream().findFirst()
        // Adding this type hint tells Java to treat the result as a generic ResponseEntity
        .<ResponseEntity<?>>map(user -> {
            user.setFullName(details.getFullName());
            user.setProfessionalTitle(details.getProfessionalTitle());
            user.setBio(details.getBio());
            user.setEmail(details.getEmail());
            user.setLocation(details.getLocation());
            user.setResumeUrl(details.getResumeUrl());
            user.setGithubUrl(details.getGithubUrl());
            user.setLinkedinUrl(details.getLinkedinUrl());
            user.setTwitterUrl(details.getTwitterUrl());

            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser); // Returns ResponseEntity<User>
        })
        .orElse(ResponseEntity.status(404).body(Map.of(
            "status", "error",
            "message", "No profile found. Please use the POST /create endpoint first."
        ))); // Returns ResponseEntity<Map>
}
    @Async("threadPoolTaskExecutor")
    @GetMapping("/profile")
    public CompletableFuture<ResponseEntity<User>> getMyProfile() {
        return CompletableFuture.supplyAsync(() -> {
            // Fetch the first user found in NeonDB
            return userRepository.findAll().stream().findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        });
    }
    @GetMapping("/summary")
    public ResponseEntity<?> getFullPortfolio() {
        // 1. Fetch the user profile
        Optional<User> userOptional = userRepository.findAll().stream().findFirst();

        // 2. If no user exists, return a 404 JSON immediately
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                "status", "error",
                "message", "No portfolio data found. Please create your profile first."
            ));
        }

        User user = userOptional.get();

        // 3. Build the response structure manually
        Map<String, Object> summary = new HashMap<>();
        
        summary.put("profile", Map.of(
            "name", user.getFullName() != null ? user.getFullName() : "",
            "title", user.getProfessionalTitle() != null ? user.getProfessionalTitle() : "",
            "bio", user.getBio() != null ? user.getBio() : "",
            "location", user.getLocation() != null ? user.getLocation() : "",
            "github", user.getGithubUrl() != null ? user.getGithubUrl() : ""
        ));

        // 4. Attach lists (Hibernate handles the join here)
        summary.put("projects", user.getProjects() != null ? user.getProjects() : List.of());
        summary.put("skills", user.getSkills() != null ? user.getSkills() : List.of());

        // 5. Return a clean 200 OK
        return ResponseEntity.ok(summary);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        return projectRepo.findById(id).map(project -> {
            // Cleanup the file first
            if (project.getImageUrl() != null) {
                storageService.delete(project.getImageUrl());
            }
            // Then delete the project
            projectRepo.delete(project);
            return ResponseEntity.ok(Map.of("message", "Project and its image deleted"));
        }).orElse(ResponseEntity.status(404).build());
    }
    }
package com.portfolio_dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/about")
@CrossOrigin(origins = "*")
public class AboutController {

    @Autowired
    private AboutRepository repository;

    // GET: Retrieve the About information
 // --- GET: Retrieve the Profile ---
    @GetMapping
    public ResponseEntity<About> getAbout() {
        return repository.findAll().stream().findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- PUT: Update the Profile ---
    @PutMapping("/{id}")
    public ResponseEntity<About> updateAbout(@PathVariable Long id, @RequestBody About aboutDetails) {
        return repository.findById(id)
            .map(about -> {
                about.setFullName(aboutDetails.getFullName());
                about.setProfessionalTitle(aboutDetails.getProfessionalTitle());
                about.setShortBio(aboutDetails.getShortBio());
                about.setSkillsAndExpertise(aboutDetails.getSkillsAndExpertise());
                about.setExperienceSummary(aboutDetails.getExperienceSummary());
                about.setMissionVision(aboutDetails.getMissionVision());
                about.setEducation(aboutDetails.getEducation());
                about.setLocation(aboutDetails.getLocation());
                about.setContactInfo(aboutDetails.getContactInfo());
                
                About updatedAbout = repository.save(about);
                return ResponseEntity.ok(updatedAbout);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // --- DELETE: Clear the Profile ---
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAbout(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("About profile deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
    // POST: Create or Update the About information
    @PostMapping
    public About saveOrUpdateAbout(@RequestBody About about) {
        // Logic: If one exists, update it. If not, create it.
        return repository.findAll().stream().findFirst()
            .map(existing -> {
                about.setId(existing.getId());
                return repository.save(about);
            })
            .orElseGet(() -> repository.save(about));
    }
}
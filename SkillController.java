package com.portfolio_dev;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillController {

    @Autowired
    private SkillRepository repository;

    @GetMapping
    public List<Skill> getAllSkills() {
        return repository.findAll();
    }

    @PostMapping
    public Skill addSkill(@RequestBody Skill skill) {
        return repository.save(skill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable Long id, @RequestBody Skill skillDetails) {
        Optional<Skill> optionalSkill = repository.findById(id);
        
        if (optionalSkill.isPresent()) {
            Skill skill = optionalSkill.get();
            skill.setName(skillDetails.getName());
            skill.setCategory(skillDetails.getCategory());
            return ResponseEntity.ok(repository.save(skill));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Skill deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}
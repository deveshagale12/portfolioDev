
import com.yourpackage.model.Skill;
import com.yourpackage.model.User;
import com.yourpackage.repository.SkillRepository;
import com.yourpackage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/skills")
@CrossOrigin(origins = "*")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * GET all skills.
     */
    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillRepository.findAll());
    }

    /**
     * POST: Add a new skill.
     * Automatically links to your single portfolio user.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addSkill(@RequestBody Skill skill) {
        return userRepository.findAll().stream()
            .findFirst()
            // Adding the type hint here solves the 'orElseGet' error
            .<ResponseEntity<?>>map(user -> {
                skill.setUser(user);
                Skill saved = skillRepository.save(skill);
                return ResponseEntity.status(201).body(saved);
            })
            .orElseGet(() -> ResponseEntity.status(404).body(Map.of(
                "status", "error",
                "message", "User profile not found. Create a profile first!"
            )));
    }

    /**
     * PUT: Update existing skill.
     * Fixed with explicit type hint to prevent Generic mismatch.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSkill(@PathVariable Long id, @RequestBody Skill skillDetails) {
        return skillRepository.findById(id)
            .<ResponseEntity<?>>map(skill -> {
                skill.setName(skillDetails.getName());
                skill.setCategory(skillDetails.getCategory());
                skill.setProficiencyLevel(skillDetails.getProficiencyLevel());
                
                return ResponseEntity.ok(skillRepository.save(skill));
            })
            .orElseGet(() -> ResponseEntity.status(404).body(Map.of(
                "status", "error",
                "message", "Skill with ID " + id + " not found"
            )));
    }

    /**
     * DELETE: Remove a skill.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long id) {
        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Skill deleted successfully"
            ));
        }
        return ResponseEntity.status(404).body(Map.of(
            "status", "error",
            "message", "Cannot delete. Skill ID " + id + " not found"
        ));
    }
}
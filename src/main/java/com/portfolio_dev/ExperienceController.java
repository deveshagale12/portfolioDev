
import com.yourpackage.model.Experience;
import com.yourpackage.repository.ExperienceRepository;
import com.yourpackage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/experience")
@CrossOrigin(origins = "*")
public class ExperienceController {

    @Autowired private ExperienceRepository experienceRepo;
    @Autowired private UserRepository userRepo;

    @PostMapping("/add")
    public ResponseEntity<?> addExperience(@RequestBody Experience exp) {
        return userRepo.findAll().stream().findFirst()
            .<ResponseEntity<?>>map(user -> {
                exp.setUser(user);
                return ResponseEntity.status(201).body(experienceRepo.save(exp));
            })
            .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Profile not found")));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(experienceRepo.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Experience details) {
        return experienceRepo.findById(id)
            .<ResponseEntity<?>>map(exp -> {
                exp.setCompanyName(details.getCompanyName());
                exp.setRole(details.getRole());
                exp.setDescription(details.getDescription());
                return ResponseEntity.ok(experienceRepo.save(exp));
            })
            .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Experience not found")));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (experienceRepo.existsById(id)) {
            experienceRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Experience deleted"));
        }
        return ResponseEntity.status(404).body(Map.of("error", "ID not found"));
    }
}
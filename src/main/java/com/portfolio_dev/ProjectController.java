

import com.yourpackage.model.Project;
import com.yourpackage.model.User;
import com.yourpackage.repository.ProjectRepository;
import com.yourpackage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * GET all projects for the portfolio.
     */
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectRepository.findAll());
    }

    /**
     * CREATE a new project.
     * Automatically links the project to the single existing user profile.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody Project project) {
        // Find your single user profile (first one in DB)
        User user = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Profile not found. Create user profile first!"));

        // Set the relationship
        project.setUser(user);
        
        Project savedProject = projectRepository.save(project);
        return ResponseEntity.ok(savedProject);
    }

    /**
     * UPDATE an existing project by ID.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        return projectRepository.findById(id).map(project -> {
            project.setTitle(projectDetails.getTitle());
            project.setDescription(projectDetails.getDescription());
            project.setTechStack(projectDetails.getTechStack());
            project.setGithubUrl(projectDetails.getGithubUrl());
            project.setLiveDemoUrl(projectDetails.getLiveDemoUrl());
            return ResponseEntity.ok(projectRepository.save(project));
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE a project.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return ResponseEntity.ok("Project deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}
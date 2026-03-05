
import com.yourpackage.model.Project; // Replace with your actual model package
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Standard CRUD operations are inherited
}
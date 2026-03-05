
import com.yourpackage.model.User; // Change to your actual package
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email (useful for login or unique identification).
     */
    Optional<User> findByEmail(String email);

    /**
     * Optimized fetch: This loads the User AND their Projects in a single query.
     * Use this in your PortfolioController to keep things fast!
     */
    @EntityGraph(attributePaths = {"projects", "profileImage"})
    Optional<User> findWithProjectsById(Long id);
}
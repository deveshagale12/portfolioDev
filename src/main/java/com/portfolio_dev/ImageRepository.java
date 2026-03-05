
import com.yourpackage.model.PortfolioImage; // Replace with your actual model package
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ImageRepository extends JpaRepository<PortfolioImage, Long> {
    
    // Custom query to find an image by its original name
    Optional<PortfolioImage> findByFileName(String fileName);
}
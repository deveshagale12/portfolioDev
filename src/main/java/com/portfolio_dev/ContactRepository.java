
import com.yourpackage.model.ContactMessage; // Change to your actual package
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.lang.Nullable;
import java.util.List;
@Repository
public interface ContactRepository extends JpaRepository<ContactMessage, Long>{

	@Nullable
	Object findAllByOrderByCreatedAtDesc();

}

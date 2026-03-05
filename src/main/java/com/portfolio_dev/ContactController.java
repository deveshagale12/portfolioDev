
import com.yourpackage.repository.ContactRepository;
import com.yourpackage.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired private ContactRepository contactRepo;
    @Autowired private EmailService emailService; // Inject the service instead

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ContactMessage msg) {
        // 1. Save to Database (Immediate)
        msg.setCreatedAt(LocalDateTime.now());
        ContactMessage saved = contactRepo.save(msg);

        // 2. Trigger the Email in the background
        emailService.sendEmailAsync(saved);
        
        // 3. Respond to the user immediately
        return ResponseEntity.status(201).body(Map.of(
            "status", "success",
            "message", "Message received! We will get back to you soon."
        ));
    }
    @GetMapping("/inbox")
    public ResponseEntity<List<ContactMessage>> getInbox() {
        // Fetches all messages from NeonDB
        return ResponseEntity.ok(contactRepo.findAll());
    }
}
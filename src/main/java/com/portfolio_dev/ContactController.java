package com.portfolio_dev;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

	@Autowired
	private ContactRepository repository;

	

	@Autowired
    private EmailService emailService; // Injecting the service here

	@PostMapping
	public ContactMessage sendMessage(@RequestBody ContactMessage message) {
	    ContactMessage saved = repository.save(message);

	    try {
	        // 1. Alert you
	        emailService.sendContactEmail(saved.getEmail(), saved.getName(), 
	                                     saved.getSubject(), saved.getDescription());

	        // 2. Auto-mail the user
	        emailService.sendThankYouEmail(saved.getEmail(), saved.getName());
	        
	    } catch (Exception e) {
	        System.err.println("Emailing failed: " + e.getMessage());
	    }

	    return saved;
	}

	
	// 2. GET: View all messages (Used by you)
	@GetMapping
	public List<ContactMessage> getAllMessages() {
		return repository.findAll();
	}

	// 3. DELETE: Remove a message
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return ResponseEntity.ok("Message deleted.");
		}
		return ResponseEntity.notFound().build();
	}
}
package com.example.finalbackend.Controller;
import com.example.finalbackend.Model.ContactUs;
import com.example.finalbackend.Service.ContactUsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/v1/contactUs")
public class ContactUsController {
    private final ContactUsService contactUsService;

    public ContactUsController(ContactUsService contactUsService) {
        this.contactUsService = contactUsService;
    }

    @PostMapping
    public ResponseEntity<ContactUs> submitContactForm(@RequestParam int clientId, @RequestBody ContactUs contactUs) {
        ContactUs savedContact = contactUsService.submitContactForm(clientId, contactUs);
        return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
    }
}

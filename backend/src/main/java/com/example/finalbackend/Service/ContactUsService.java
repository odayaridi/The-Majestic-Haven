package com.example.finalbackend.Service;
import com.example.finalbackend.Exception.ResourceNotFoundException;
import com.example.finalbackend.Model.Client;
import com.example.finalbackend.Model.ContactUs;
import com.example.finalbackend.Repository.ClientRepository;
import com.example.finalbackend.Repository.ContactUsRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactUsService {
    private final ContactUsRepository contactUsRepository;
    private final ClientRepository clientRepository;

    public ContactUsService(ContactUsRepository contactUsRepository, ClientRepository clientRepository) {
        this.contactUsRepository = contactUsRepository;
        this.clientRepository = clientRepository;
    }

    public ContactUs submitContactForm(int clientId, ContactUs contactUs) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
        contactUs.setClient(client);
        return contactUsRepository.save(contactUs);
    }
}

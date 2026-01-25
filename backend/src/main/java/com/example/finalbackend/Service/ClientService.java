package com.example.finalbackend.Service;

import com.example.finalbackend.Exception.ResourceNotFoundException;
import com.example.finalbackend.Model.Client;
import com.example.finalbackend.Repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void deleteClientService(Integer clientId) {
        if (clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
        } else {
            throw new EntityNotFoundException("Client with ID " + clientId + " not found.");
        }
    }



    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registerClientService(Client client) {
        // Hash the plain password
        String hashedPassword = passwordEncoder.encode(client.getClientPassword());
        client.setClientPassword(hashedPassword);
        return clientRepository.save(client);
    }

    public Client authenticateClient(String email, String rawPassword) {
    Client client = clientRepository.findByClientEmailIgnoreCase(email);
    if (client != null && passwordEncoder.matches(rawPassword, client.getClientPassword())) {
        return client;
    }
    return null;
    }


    public boolean isEmailRegisteredService(String email){
        return clientRepository.existsByClientEmailIgnoreCase(email);
    }


    public Client fetchClientCredentialsService(String email) {
        Client client= clientRepository.findClientByClientEmail(email);
        if(client==null){
           throw new ResourceNotFoundException("Cannot retrieve this client to fetch the information!");
        }
        return client;
    }

    public Client updateClientCredentialsService(Integer id, Client client) {
        Client existingClient = clientRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Cannot retrieve this client to update credentials")
        );

        existingClient.setClientEmail(client.getClientEmail());
        existingClient.setClientFirstName(client.getClientFirstName());
        existingClient.setClientLastName(client.getClientLastName());
        String hashedPassword = passwordEncoder.encode(client.getClientPassword());
        existingClient.setClientPassword(hashedPassword);
        return clientRepository.save(existingClient);
    }

}

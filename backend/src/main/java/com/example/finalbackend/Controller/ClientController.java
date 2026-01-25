package com.example.finalbackend.Controller;

import com.example.finalbackend.Exception.ResourceNotFoundException;
import com.example.finalbackend.Model.Client;
import com.example.finalbackend.Service.ClientService;
import com.example.finalbackend.Security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    private final JwtUtil jwtUtil;

    public ClientController(ClientService clientService, JwtUtil jwtUtil) {
        this.clientService = clientService;
        this.jwtUtil = jwtUtil;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable Integer id) {
        try{
        clientService.deleteClientService(id);
        return new ResponseEntity<>("DELETED",HttpStatus.OK);
    }
        catch (Exception e){
            return new ResponseEntity<>("Error",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Object> registerClient(@Valid @RequestBody Client client, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("invalid credentials");
        }
        Client newClient = clientService.registerClientService(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginClient(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("clientEmail");
        String password = credentials.get("clientPassword");
        if(email.isEmpty() || password.isEmpty()){
            return ResponseEntity.badRequest().body("Empty Credentials");
        }
        Client client = clientService.authenticateClient(email, password);
        Map<String, String> response = new HashMap<>();

        if (client != null) {
            String token = jwtUtil.generateToken(email);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/registered")
    public ResponseEntity<Map<String, Boolean>> isEmailRegistered(@RequestParam String email) {
        boolean isRegistered = clientService.isEmailRegisteredService(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("registered", isRegistered);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Object> getClientByEmail(@PathVariable String email) {
        try{
            return ResponseEntity.ok(clientService.fetchClientCredentialsService(email));
        }
        catch (ResourceNotFoundException re){
            return new ResponseEntity<>(re.getMessage(), HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateClientCredentials(@PathVariable Integer id,
                                                          @Valid @RequestBody Client client,
                                                          BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Error in updating existing client");
        }
        try {
            Client updatedClient = clientService.updateClientCredentialsService(id, client);
            return ResponseEntity.ok(updatedClient);
        }
        catch (ResourceNotFoundException re){
            return new ResponseEntity<>(re.getMessage(), HttpStatus.CREATED);
        }
    }
}

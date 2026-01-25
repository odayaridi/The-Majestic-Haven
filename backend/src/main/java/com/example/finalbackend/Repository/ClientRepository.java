package com.example.finalbackend.Repository;
import com.example.finalbackend.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ClientRepository extends JpaRepository<Client,Integer> {
    boolean existsByClientEmailIgnoreCase(String clientEmail);
    Client findClientByClientEmail(String clientEmail);
    Client findByClientEmailIgnoreCase(String email);
}

package com.example.finalbackend.Security;
import com.example.finalbackend.Model.Client;
import com.example.finalbackend.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class ClientInfoService implements UserDetailsService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByClientEmailIgnoreCase(email);
        if(client ==null){
            throw new UsernameNotFoundException("Client not found");
        }
        return User.builder()
                .username(client.getClientEmail())
                .password(client.getClientPassword())
                .roles("USER")
                .build();
    }
}

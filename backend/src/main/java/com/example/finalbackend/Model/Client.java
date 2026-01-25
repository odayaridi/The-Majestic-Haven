package com.example.finalbackend.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="Clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer clientId;

    @NotEmpty(message = "First Name cannot be empty!")
    @Column(name = "first_name", nullable = false, length = 50)
    private String clientFirstName;

    @NotEmpty(message = "Last Name cannot be empty!")
    @Column(name = "last_name")
    private String clientLastName;

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Invalid email format!")
    @Column(name = "email", nullable = false, unique = true)
    private String clientEmail;

    @NotEmpty(message = "Password cannot be empty!")
    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clientPassword;

    public Client() {
    }

    public Client(Integer clientId, String clientFirstName, String clientLastName, String clientEmail, String clientPassword) {
        this.clientId = clientId;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.clientEmail = clientEmail;
        this.clientPassword = clientPassword;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public @NotEmpty(message = "First Name cannot be empty!") String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(@NotEmpty(message = "First Name cannot be empty!") String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public @NotEmpty(message = "Last Name cannot be empty!") String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(@NotEmpty(message = "Last Name cannot be empty!") String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public @NotEmpty(message = "Email cannot be empty!") @Email(message = "Invalid email format!") String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(@NotEmpty(message = "Email cannot be empty!") @Email(message = "Invalid email format!") String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public @NotEmpty(message = "Password cannot be empty!")  String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(@NotEmpty(message = "Password cannot be empty!")  String clientPassword) {
        this.clientPassword = clientPassword;
    }


    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", clientFirstName='" + clientFirstName + '\'' +
                ", clientLastName='" + clientLastName + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", clientPassword='" + clientPassword + '\'' +
                '}';
    }
}

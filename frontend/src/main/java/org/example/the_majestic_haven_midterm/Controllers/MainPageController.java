package org.example.the_majestic_haven_midterm.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.example.the_majestic_haven_midterm.APiInterface.APi;
import org.example.the_majestic_haven_midterm.Singleton.ClientSession;
import org.example.the_majestic_haven_midterm.Models.Client;
import java.io.IOException;
import java.util.Objects;
import org.example.the_majestic_haven_midterm.Storage.TokenStorage;
import org.json.JSONObject;

public class MainPageController {

    @FXML
    private TextField emailLogin;

    @FXML
    private TextField emailRegister;

    @FXML
    private TextField firstNameRegister;

    @FXML
    private TextField lastNameRegister;

    @FXML
    private Button loginButton;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private PasswordField passwordLogin;

    @FXML
    private PasswordField passwordRegister;

    private MediaPlayer mediaPlayer;

    @FXML
    private AnchorPane registerPane;

    @FXML
    void havingAnAccountClick(MouseEvent event) {
        registerPane.setVisible(false);
        loginPane.setVisible(true);
    }

    @FXML
    void notHavingAnAccountClick(MouseEvent event) {
        registerPane.setVisible(true);
        loginPane.setVisible(false);
    }

    @FXML
    void registerClick(ActionEvent event) {
        loadButtonSound();
        boolean accessDenied = false;
        String firstNameString = firstNameRegister.getText().trim();
        String lastNameString = lastNameRegister.getText().trim();
        String emailString = emailRegister.getText().trim();
        String passwordString = passwordRegister.getText().trim();

        if (firstNameString.isEmpty() || lastNameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty()) {
            showAlert("Error", "Incomplete Entry of information!", 0);
            accessDenied = true;
        } else {
            if (!emailString.contains("@") || !(emailString.endsWith("@gmail.com") ||
                    emailString.endsWith("@hotmail.com") || emailString.endsWith("@yahoo.com"))) {
                showAlert("Error", "Invalid Email Address!", 0);
                accessDenied = true;
            }

            if (passwordString.length() < 6) {
                showAlert("Error", "Password length should be at least 6 characters!", 0);
                accessDenied = true;
            }

            String checkURL = "http://localhost:8083/api/v1/clients/registered?email=" + emailString;
            String checkResponse = APi.executeAPI("GET", checkURL, null,null);
            if (checkResponse != null && checkResponse.contains("true")) {
                showAlert("Access Denied", "User account already exists with this email address!", 0);
                accessDenied = true;
            }
        }

        if (!accessDenied) {
            String payload = """
    {
        "clientFirstName": "%s",
        "clientLastName": "%s",
        "clientEmail": "%s",
        "clientPassword": "%s"
    }
    """.formatted(firstNameString, lastNameString, emailString, passwordString);
            String response = APi.executeAPI("POST", "http://localhost:8083/api/v1/clients", payload,null);
            if (response != null) {
                showAlert("Thank you", "Registered Successfully!", 1);
                registerPane.setVisible(false);
                loginPane.setVisible(true);
            } else {
                showAlert("Error", "Registration failed!", 0);
            }
        }
    }

    @FXML
    void loginClick(ActionEvent event) throws IOException {
        loadButtonSound();
        String emailString = emailLogin.getText().trim();
        String passwordString = passwordLogin.getText().trim();

        if (emailString.isEmpty() || passwordString.isEmpty()) {
            showAlert("Access Denied", "Empty Credentials!", 0);
        } else {
            String url = "http://localhost:8083/api/v1/clients/login";
            String json = """
        {
            "clientEmail": "%s",
            "clientPassword": "%s"
        }
        """.formatted(emailString, passwordString);
            String response = APi.executeAPI("POST", url, json, null);
            if (response != null && response.contains("token")) {
                JSONObject jsonObject = new JSONObject(response);
                String token = jsonObject.getString("token");
                TokenStorage.setToken(token);
                String fetchUrl = "http://localhost:8083/api/v1/clients/email/" + emailString;
                String newJsonResponse = APi.executeAPI("GET", fetchUrl, null, token);
                JSONObject obj = new JSONObject(newJsonResponse);
                Client newClient = new Client(
                        obj.getInt("clientId"),
                        obj.getString("clientFirstName"),
                        obj.getString("clientLastName"),
                        obj.getString("clientEmail"),
                        null
                );

                if (ClientSession.getInstance() != null)
                    ClientSession.getInstance().setCurrentUser(newClient);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/app-page.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.show();
                loginButton.getScene().getWindow().hide();
            } else {
                showAlert("Access Denied", "Invalid Credentials!", 0);
            }
        }
    }

    private void showAlert(String title, String message, int polarity) {
        Alert alert;
        if(polarity==0){
            alert=new Alert(Alert.AlertType.ERROR);
        }
        else{
            alert=new Alert(Alert.AlertType.CONFIRMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Sound on button click
    private void loadButtonSound() {
        mediaPlayer = new MediaPlayer(new Media(Objects.requireNonNull(
                getClass().getResource("/org/example/the_majestic_haven_midterm/MediaPlayer/button_click.mp3")).toString()));
        mediaPlayer.play();
    }
}

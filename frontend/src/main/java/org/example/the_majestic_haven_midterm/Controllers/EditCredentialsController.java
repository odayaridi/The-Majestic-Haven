package org.example.the_majestic_haven_midterm.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.the_majestic_haven_midterm.APiInterface.APi;
import org.example.the_majestic_haven_midterm.Singleton.ClientSession;
import org.example.the_majestic_haven_midterm.Models.Client;
import org.example.the_majestic_haven_midterm.Storage.TokenStorage;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditCredentialsController implements Initializable {

    @FXML
    private TextField currentPass;

    @FXML
    private TextField email;

    @FXML
    private TextField newFirstName;

    @FXML
    private TextField newLastName;

    @FXML
    private TextField newPass;


    private Client client= ClientSession.getInstance().getCurrentUser();

    private final String token = TokenStorage.getToken();

    private  MediaPlayer mediaPlayer;


    @FXML
    void onClickUpdateButton(MouseEvent event) {
        loadButtonSound();
        String clientEmail = client.getEmail();
        String oldPassword = currentPass.getText().trim();
        String newFName = newFirstName.getText().trim();
        String newLName = newLastName.getText().trim();
        String newPasswordString = newPass.getText().trim();

        boolean accessDenied = false;

        if (oldPassword.isEmpty() || newFName.isEmpty() || newLName.isEmpty() || newPasswordString.isEmpty()) {
            showAlert("Error", "Empty Credentials!", 0);
            accessDenied = true;
        } else if (newPasswordString.length() < 6) {
            showAlert("Error", "Password length should be at least 6 characters!", 0);
            accessDenied = true;
        }

        if (!accessDenied) {
            String url = "http://localhost:8083/api/v1/clients/" + client.getClientId();

            String json = """
            {
                "clientFirstName": "%s",
                "clientLastName": "%s",
                "clientEmail": "%s",
                "clientPassword": "%s"
            }
            """.formatted(newFName, newLName, clientEmail, newPasswordString);

            try {
                String response = APi.executeAPI("PUT", url, json, token);

                if (response != null && response.contains("clientEmail")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Profile Updated");
                    alert.setHeaderText("Credentials updated successfully!");
                    alert.setContentText("You must re-login. Click OK to proceed.");
                    Optional<ButtonType> result = alert.showAndWait();
                    ClientSession.getInstance().clearSession();
                    TokenStorage.clearToken();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/main-page.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setResizable(false);
                    stage.setScene(new Scene(root));
                    stage.show();
                    Stage currentStage = (Stage) currentPass.getScene().getWindow();
                    currentStage.close();
                } else {
                    System.out.println("Error updating profile. Response: " + response);
                    showAlert("Update Failed", "An error occurred while updating your profile.", 0);
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Exception", "Something went wrong: " + e.getMessage(), 0);
            }
        }
    }


    //Reusable showAlert Method
    private void showAlert(String title, String message,int polarity) {
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

    private void loadButtonSound(){
        mediaPlayer = (new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/MediaPlayer/button_click.mp3")).toString())));
        mediaPlayer.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        email.setText(client.getEmail());
        email.setEditable(false);
    }
}

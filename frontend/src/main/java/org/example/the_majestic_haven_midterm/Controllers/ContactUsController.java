package org.example.the_majestic_haven_midterm.Controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.the_majestic_haven_midterm.APiInterface.APi;
import org.example.the_majestic_haven_midterm.Singleton.ClientSession;
import org.example.the_majestic_haven_midterm.Models.Client;
import org.example.the_majestic_haven_midterm.Storage.TokenStorage;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ContactUsController implements Initializable {

    @FXML
    private TextField message;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField savedEmail;

    @FXML
    private TextField savedFullName;

    @FXML
    private ComboBox<String> subjectComboBox;

    private  MediaPlayer mediaPlayer;

    private final String token = TokenStorage.getToken();

    private Client client = ClientSession.getInstance().getCurrentUser();

    @FXML
    void onClickSendButton(MouseEvent event) {
        loadButtonSound();
       String phNumber = phoneNumber.getText().trim();
       String userOption = subjectComboBox.getValue();
       String clientMessage = message.getText().trim();
       boolean invalidAccess = false;
       int countDigits=0;

       if(phNumber.isEmpty() || userOption ==null || clientMessage.isEmpty()){
           showAlert("Error","Empty Credentials!",0);
           invalidAccess=true;
       }
       else{ //Check phone number should only contain one letter at the beginning (+)
           for (int i = 1; i < phNumber.length(); i++) {
               if (Character.isDigit(phNumber.charAt(i))) {
                   countDigits++;
               }
           }
           if (phNumber.charAt(0) != '+' && countDigits != phNumber.length() - 2) {
               showAlert("Error", "Invalid phone number format!", 0);
               invalidAccess = true;
           }
       }
       if(!invalidAccess){

           String url  = "http://localhost:8083/api/v1/contactUs?clientId=" + client.getClientId();
           String json = """
                   {
                   "fullName": "%s",
                   "email": "%s",
                   "phone": "%s",
                   "subject": "%s",
                   "message": "%s"
                   }
                   """.formatted(savedFullName.getText(),savedEmail.getText(),phNumber,userOption,clientMessage);

           String response = APi.executeAPI("POST", url, json, token);

           if (response != null && response.contains("subject")) {
               showAlert("Thank you", "Form submitted successfully", 1);
               message.setText("");
               phoneNumber.setText("");
           }
           else {
               System.out.println("Internal error exists");
           }
       }

    }

    private void putClientInfo() {
        if (client != null) {
            savedEmail.setText(client.getEmail());
            String firstName = client.getFirstName();
            String lastName = client.getLastName();
            String formattedFirstName = Character.toUpperCase(firstName.charAt(0)) + "";
            String formattedLastName = Character.toUpperCase(lastName.charAt(0)) + "";
            for (int i = 1; i < firstName.length(); i++) {
                formattedFirstName += Character.toLowerCase(firstName.charAt(i));
            }
            for (int i = 1; i < lastName.length(); i++) {
                formattedLastName += Character.toLowerCase(lastName.charAt(i));
            }
            savedFullName.setText(formattedFirstName + " " + formattedLastName);
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
        putClientInfo();
    }
}

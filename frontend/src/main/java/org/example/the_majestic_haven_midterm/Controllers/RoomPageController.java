package org.example.the_majestic_haven_midterm.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.the_majestic_haven_midterm.APiInterface.APi;
import org.example.the_majestic_haven_midterm.Singleton.ClientSession;
import org.example.the_majestic_haven_midterm.Models.Client;
import org.example.the_majestic_haven_midterm.Models.Room;
import org.example.the_majestic_haven_midterm.Singleton.SharedStackPane;
import org.example.the_majestic_haven_midterm.Storage.TokenStorage;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class RoomPageController {

    @FXML
    private AnchorPane applyRentPane;

    @FXML
    private AnchorPane cancelRentPane;

    @FXML
    private AnchorPane feedbackPane;

    @FXML
    private ComboBox<String> rentingTimeComboBox;

    @FXML
    private Label roomAvailability;

    @FXML
    private ImageView roomImageView;

    @FXML
    private Label roomName;

    @FXML
    private Label roomNumber;

    @FXML
    private Label roomPrice;

    @FXML
    private Label roomType;

    @FXML
    private TextField userInputFeedback;

    private Room room;

    private Client client = ClientSession.getInstance().getCurrentUser();

    private StackPane parentStackPane = SharedStackPane.getInstance().getParentStackPane();

    private VBox allRoomsPane;

    private MediaPlayer mediaPlayer;

    private final String token = TokenStorage.getToken();

    @FXML
    void onChooseOptionCB(ActionEvent event) {
        String rentingTimeString = rentingTimeComboBox.getValue();

        if (rentingTimeString.equals("1 day")) {
            roomPrice.setText(String.valueOf(room.getRoomPrice()));
        }
        else if (rentingTimeString.equals("3 days")) {
            roomPrice.setText(String.valueOf(room.getRoomPrice() * 3));
        }
        else if (rentingTimeString.equals("1 week")) {
            roomPrice.setText(String.valueOf(room.getRoomPrice() * 7));
        }
        else if (rentingTimeString.equals("3 weeks")) {
            roomPrice.setText(String.valueOf(room.getRoomPrice() * 21));
        }
        else { // "1 month"
            roomPrice.setText(String.valueOf(room.getRoomPrice() * 30));
        }
    }


    @FXML
    void onClickBackButton(ActionEvent event) {
        loadButtonSound();
        try {
            allRoomsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/room-section.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setPane(allRoomsPane);
    }


    @FXML
    void onClickRentButton(ActionEvent event) {
        loadButtonSound();

        double roomRentPrice = Double.parseDouble(roomPrice.getText());
        LocalDate checkIn = LocalDate.now();
        LocalDateTime checkOut;

        if (roomRentPrice == room.getRoomPrice()) {
            checkOut = LocalDateTime.now().plusDays(1);
        } else if (roomRentPrice == room.getRoomPrice() * 3) {
            checkOut = LocalDateTime.now().plusDays(3);
        } else if (roomRentPrice == room.getRoomPrice() * 7) {
            checkOut = LocalDateTime.now().plusWeeks(1);
        } else if (roomRentPrice == room.getRoomPrice() * 21) {
            checkOut = LocalDateTime.now().plusWeeks(3);
        } else if (roomRentPrice == room.getRoomPrice() * 30) {
            checkOut = LocalDateTime.now().plusDays(30);
        } else {
            checkOut = LocalDateTime.now().plusDays(1);
        }

        String url = "http://localhost:8083/api/v1/bookings/clientId/" + client.getClientId() + "/roomId/" + room.getRoomId();
        String json = """
            {
            "bookingCheckInDate": "%s",
            "bookingCheckOutDate":"%s",
            "bookingTotalPrice":%s
            }
            """.formatted(Date.valueOf(checkIn), Date.valueOf(checkOut.toLocalDate()), roomRentPrice);

        String response = APi.executeAPI("POST", url, json, token);

        if (response != null && response.contains("bookingTotalPrice")) {
            applyRentPane.setVisible(false);
            cancelRentPane.setVisible(true);
            String statusText = "Room Rented till " + checkOut.toLocalDate();
            if (updateRoomStatusAPI(room.getRoomId(), statusText)) {
                room.setAvailability_status(statusText);
                roomAvailability.setText(statusText);
                roomAvailability.setStyle("-fx-text-fill: red");
                feedbackPane.setVisible(true);
                showAlert("Enjoy", "Please contact +9610397846 for payment information! ", 1);
            } else {
                System.err.println("Error in updating availability status");
            }
        } else {
            System.err.println("Error in creating booking!");
        }
    }

    @FXML
    void onClickCancelRentButton(ActionEvent event) {
        loadButtonSound();
            if(deleteBookingAPI()){
                if(updateRoomStatusAPI(room.getRoomId(),"Available")){
                    roomAvailability.setText("Available");
                    room.setAvailability_status("Available");
                    roomAvailability.setStyle("-fx-text-fill: #1A237E");
                    applyRentPane.setVisible(true);
                    cancelRentPane.setVisible(false);
                    feedbackPane.setVisible(false);
                    showAlert("Successful","Booking Cancelled!",1);
                }
                else {
                    System.err.println("Error in updating status!");
                }
            }
            else {
                System.err.println("Error in deleting booking");
            }
    }

    @FXML
    void onClickFeedbackButton(MouseEvent event) {
        loadButtonSound();
        if(userInputFeedback.getText().isEmpty()){
            showAlert("Error","You can't submit empty feedback",0);
        }
        else {
            String url = "http://localhost:8083/api/v1/reviews?cId=" + client.getClientId() + "&rId=" +
                    room.getRoomId();
            String json  = """
    {
      "reviewText": "%s"
    }
    """.formatted(userInputFeedback.getText().trim());

            String response = APi.executeAPI("POST",url,json,token);
            if(response!=null && response.contains("reviewText")){
                showAlert("Success","Your feedback is submitted successfully!",1);
            }
            else{
                System.err.println("Error happened!");
            }


        }
    }

    public void setPane(Parent pane){
        if (!parentStackPane.getChildren().isEmpty()) {
            parentStackPane.getChildren().clear();
        }
        parentStackPane.getChildren().add(pane);
    }

    public void setRoomInfo(Room r) {
        if (r != null) {
            this.room = r;
            roomName.setText(room.getRoomName());
            roomPrice.setText(String.valueOf(room.getRoomPrice()));
            roomType.setText(room.getRoomType());
            roomNumber.setText(room.getRoomNumber());
            roomAvailability.setText(room.getAvailability_status());
            roomImageView.setImage(new Image(getClass().getResource(room.getImage()).toExternalForm()));
        }
        else {
            System.out.println("Error occurred when rendering");
        }
        checkRenterEnter();
        removeExpiredBooking();
    }



    private void checkRenterEnter() {
        String url = "http://localhost:8083/api/v1/bookings/clientId/" + client.getClientId() + "/roomId/" +
                room.getRoomId();
        String response = APi.executeAPI("GET",url,null,token);
        if(response!=null && response.contains("rented")){
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getBoolean("rented")){
                applyRentPane.setVisible(false);
                cancelRentPane.setVisible(true);
                feedbackPane.setVisible(true);
                roomAvailability.setStyle("-fx-text-fill: red");
            }
            else{
                feedbackPane.setVisible(false);
                String urlTwo = "http://localhost:8083/api/v1/rooms/availability?roomId=" + room.getRoomId();
                String responseTwo = APi.executeAPI("GET",urlTwo,null,token);
                if(responseTwo!=null && responseTwo.contains("Availability")){
                    JSONObject jsonObjectTwo = new JSONObject(responseTwo);
                    if(jsonObjectTwo.getString("Availability").equals("Available")){
                        applyRentPane.setVisible(true);
                        cancelRentPane.setVisible(false);
                        roomAvailability.setStyle("-fx-text-fill: #1A237E");
                    }
                    else{
                        applyRentPane.setVisible(false);
                        cancelRentPane.setVisible(false);
                        roomAvailability.setStyle("-fx-text-fill: red");
                    }
                }
                else{
                    System.err.println("Error in checking room availability!");
                }
            }

        }
        else{
            System.err.println("Error happened when checking room rental!");
        }
 }

    private void removeExpiredBooking() {
        String url = "http://localhost:8083/api/v1/bookings/checkoutDate?roomId=" + room.getRoomId();
        String response = APi.executeAPI("GET", url, null, token);
        if (response != null && response.contains("checkOutDate")) {
            JSONObject jsonObject = new JSONObject(response);
            LocalDate checkOutDate = LocalDate.parse(jsonObject.getString("checkOutDate"));
            if (LocalDate.now().isAfter(checkOutDate) || LocalDate.now().isEqual(checkOutDate)) {
                String deleteUrl = "http://localhost:8083/api/v1/bookings/room/" + room.getRoomId();
                String deleteResponse = APi.executeAPI("DELETE", deleteUrl, null, token);
                if ("".equals(deleteResponse)) {
                    if (updateRoomStatusAPI(room.getRoomId(), "Available")) {
                        System.out.println("4th if statement executed");
                        roomAvailability.setText("Available");
                        roomAvailability.setStyle("-fx-text-fill: #1A237E");
                        applyRentPane.setVisible(true);
                        cancelRentPane.setVisible(false);
                        feedbackPane.setVisible(false);
                    }
                } else {
                    System.err.println("Failed to delete expired booking");
                }
            }
        } else {
            System.out.println("Room not booked!");
        }
    }


    private boolean updateRoomStatusAPI(int roomId, String text){
        String url = "http://localhost:8083/api/v1/rooms/" + roomId
                + "?availabilityStatus=" + URLEncoder.encode(text, StandardCharsets.UTF_8).replace("+", "%20");
        String response = APi.executeAPI("PUT",url,null ,token);
        if(response!=null) {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getBoolean("statusUpdated");
        }
        return false;
    }
    private boolean deleteBookingAPI(){
        String url ="http://localhost:8083/api/v1/bookings?clientId=" + client.getClientId() + "&roomId=" +
                room.getRoomId();
        String response = APi.executeAPI("DELETE",url,null,token);
        return response!=null && response.isEmpty();
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
}

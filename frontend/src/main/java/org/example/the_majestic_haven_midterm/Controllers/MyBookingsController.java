package org.example.the_majestic_haven_midterm.Controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.the_majestic_haven_midterm.APiInterface.APi;
import org.example.the_majestic_haven_midterm.Models.Room;
import org.example.the_majestic_haven_midterm.Singleton.ClientSession;
import org.example.the_majestic_haven_midterm.Models.Booking;
import org.example.the_majestic_haven_midterm.Models.Client;
import org.example.the_majestic_haven_midterm.Storage.TokenStorage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class MyBookingsController implements Initializable {

    @FXML
    private TableView<Booking> clientTable;

    @FXML
    private TableColumn<Booking, String> rentEndDate;

    @FXML
    private TableColumn<Booking, String> rentStartDate;

    @FXML
    private TableColumn<Booking, String> roomName;

    @FXML
    private TableColumn<Booking, String> roomNumberColumn;

    @FXML
    private TableColumn<Booking, Double> roomPrice;

    @FXML
    private ComboBox<String> rentingTimeComboBox;

    @FXML
    private TextField userInput;

    @FXML
    private Label priceShow;

    private MediaPlayer mediaPlayer;

    private Client client = ClientSession.getInstance().getCurrentUser();

    private final String token = TokenStorage.getToken();


    @FXML
    void onClickRefreshButton(ActionEvent event) {
        loadButtonSound();
        showClientBookings(client.getClientId());
    }

    private void showClientBookings(int clientId){
        String url = "http://localhost:8083/api/v1/bookings?cId=" + clientId;
        String response = APi.executeAPI("GET",url,null,token);
        ObservableList<Booking> bookingsList = FXCollections.observableArrayList();
        if(response!=null) {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                bookingsList.add(new Booking(
                        jsonObject.getString("roomNumber"),
                        jsonObject.getString("roomName"),
                        jsonObject.getString("checkInDate"),
                        jsonObject.getString("checkOutDate"),
                        jsonObject.getDouble("totalPrice")
                ));
            }
            roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            roomName.setCellValueFactory(new PropertyValueFactory<>("roomName"));
            roomPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            rentStartDate.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
            rentEndDate.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
            clientTable.setItems(bookingsList);
        }
        else{
            System.err.println("Error happened in fetching client bookings!");
        }
    }


    @FXML
    public void onChooseOptionCB(ActionEvent event) {
        priceShow.setMaxHeight(Double.MAX_VALUE);
        priceShow.setAlignment(Pos.CENTER);
        Room room = null;
        if (isRoomNumber(userInput.getText())) {
            String url = "http://localhost:8083/api/v1/rooms/roomNumber/" + userInput.getText();
            String response = APi.executeAPI("GET", url, null, token);
            if (response != null) {
                room = fetchRoom(response);
            } else {
                System.err.println("room not found!");
            }
        } else {
            String url = "http://localhost:8083/api/v1/rooms/roomName/" + URLEncoder.encode(userInput.getText(), StandardCharsets.UTF_8).replace("+", "%20");
            String response = APi.executeAPI("GET", url, null, token);
            if (response != null) {
                room = fetchRoom(response);
            } else {
                System.err.println("room not found!");
            }
        }
        if (room != null){
            String rentingTimeString = rentingTimeComboBox.getValue();
            if (rentingTimeString.equals("1 day")) {
                priceShow.setText("Price: " + room.getRoomPrice() + "$");
            } else if (rentingTimeString.equals("3 days")) {
                priceShow.setText("Price: " + (room.getRoomPrice() * 3) + "$");
            } else if (rentingTimeString.equals("1 week")) {
                priceShow.setText("Price: " + (room.getRoomPrice() * 7) + "$");
            } else if (rentingTimeString.equals("3 weeks")) {
                priceShow.setText("Price: " + (room.getRoomPrice() * 21) + "$");
            } else { // "1 month"
                priceShow.setText("Price: " + (room.getRoomPrice() * 30) + "$");
            }
        }
        else {
            priceShow.setText("Price: " + 0 + "$");
        }
    }

    @FXML
    void onClickExtendRent(MouseEvent event) {
        String userInputText = userInput.getText();
        if(userInputText.isEmpty()){
            showAlert("Error","Empty Input!",0);
        }
        else {
            if (isRoomNumber(userInputText)) {
                String url ="http://localhost:8083/api/v1/rooms/roomNumber/" + userInputText;
                String response = APi.executeAPI("GET",url,null,token);
                if(response!=null){
                    if(checkRoomRentedByThisClient(fetchRoom(response).getRoomId())){
                        extendClientBooking(fetchRoom(response).getRoomId());
                    }
                    else{
                        showAlert("Access Denied","This room is not already rented by yours!",0);
                    }
                }
                else{
                    showAlert("Error","Invalid Room Number!",0);
                }

            } else {
                String url = "http://localhost:8083/api/v1/rooms/roomName/" + URLEncoder.encode(userInputText, StandardCharsets.UTF_8).replace("+", "%20");
                String response = APi.executeAPI("GET",url,null,token);
                if(response!=null){
                    if(checkRoomRentedByThisClient(fetchRoom(response).getRoomId())){
                        extendClientBooking(fetchRoom(response).getRoomId());
                    }
                    else{
                        showAlert("Access Denied","This room is not already rented by yours!",0);
                    }
                }
                else{
                    showAlert("Error","Invalid Room Name!",0);
                }
            }
        }
    }

    private void extendClientBooking(Integer roomId) {
        loadButtonSound();
        String value = rentingTimeComboBox.getValue();
        String currentDateString = getRoomBookingCurrentDate(roomId);

        if (currentDateString == null) {
            showAlert("Error", "Could not fetch current check-out date!", 0);
            return;
        }

        LocalDate currentCheckOutDate = LocalDate.parse(currentDateString);
        LocalDate newCheckOutDate;

        if (value.equals("1 day")) {
            newCheckOutDate = currentCheckOutDate.plusDays(1);
        } else if (value.equals("3 days")) {
            newCheckOutDate = currentCheckOutDate.plusDays(3);
        } else if (value.equals("1 week")) {
            newCheckOutDate = currentCheckOutDate.plusWeeks(1);
        } else if (value.equals("3 weeks")) {
            newCheckOutDate = currentCheckOutDate.plusWeeks(3);
        } else {
            newCheckOutDate = currentCheckOutDate.plusMonths(1);
        }
        String formattedDate = newCheckOutDate.toString();
        String url = "http://localhost:8083/api/v1/bookings/cId/" + client.getClientId()
                + "/rId/" + roomId + "?updatedCheckOutDate=" + formattedDate;
        String response = APi.executeAPI("PUT", url, null, token);
        if (response != null) {
            showAlert("Thank You", "Room date successfully extended!", 1);
        } else {
            showAlert("Error", "Error occurred while doing the operation!", 0);
        }
    }
    private boolean checkRoomRentedByThisClient(Integer roomId){
        String url = "http://localhost:8083/api/v1/bookings/clientId/" + client.getClientId() + "/roomId/" +
                roomId;
        String response = APi.executeAPI("GET",url,null,token);
        if(response!=null && response.contains("rented")){
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getBoolean("rented");
        }
        else{
            System.err.println("Error happened!");
            return false;
        }
    }

    private String getRoomBookingCurrentDate(Integer roomId){
        String url = "http://localhost:8083/api/v1/bookings/checkoutDate?roomId=" + roomId;
        String response = APi.executeAPI("GET",url,null,token);
        if(response!=null && response.contains("checkOutDate")){
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("checkOutDate");
        }
        System.err.println("Room does not exist!");
        return null;
    }


    private Room fetchRoom(String response) {
        JSONObject jsonObject = new JSONObject(response);
        return new Room(jsonObject.getInt("roomId"), jsonObject.getString("roomName"),
                jsonObject.getString("roomType"), jsonObject.getString("roomNumber"),
                jsonObject.getDouble("roomPricePerNight"), jsonObject.getString("roomAvailabilityStatus"),
                jsonObject.getString("roomImage"));
    }


    public boolean isRoomNumber(String input){
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    private void loadButtonSound(){
        mediaPlayer = (new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/MediaPlayer/button_click.mp3")).toString())));
        mediaPlayer.play();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showClientBookings(client.getClientId());
        userInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (rentingTimeComboBox.getValue() != null && !newValue.isEmpty()) {
                onChooseOptionCB(null);
            }
        });
        rentingTimeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !userInput.getText().isEmpty()) {
                onChooseOptionCB(null);
            }
        });
    }
}

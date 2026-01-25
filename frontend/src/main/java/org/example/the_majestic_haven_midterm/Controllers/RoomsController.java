package org.example.the_majestic_haven_midterm.Controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.the_majestic_haven_midterm.APiInterface.APi;
import org.example.the_majestic_haven_midterm.Models.Room;
import org.example.the_majestic_haven_midterm.Singleton.SharedStackPane;
import org.example.the_majestic_haven_midterm.Storage.TokenStorage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RoomsController implements Initializable {

    @FXML
    private TextField searchbar;

    @FXML
    private TilePane tilePane;

    private StackPane parentStackPane = SharedStackPane.getInstance().getParentStackPane();

    private VBox roomInfoPane;

    private RoomPageController roomPageController;

    private  MediaPlayer mediaPlayer;

    private final String token = TokenStorage.getToken();


    @FXML
    void userInputType(KeyEvent event) {
        onClickSearchButton();
    }
    @FXML
    void onClickRefreshButton() {
        loadButtonSound();
        refreshPage();
    }

    @FXML
    void onClickSearchButton() {
//        loadButtonSound();
        if (!tilePane.getChildren().isEmpty()) {
            tilePane.getChildren().clear();
        }
        String searchBarText = searchbar.getText();

        if(searchBarText.isEmpty()){
           refreshPage();
        }
        else if (searchBarText.equalsIgnoreCase("Single") ||
                searchBarText.equalsIgnoreCase("Double") ||
                searchBarText.equalsIgnoreCase("Suite")) {
            String url ="http://localhost:8083/api/v1/rooms/roomType/" + searchBarText;
            String response = APi.executeAPI("GET",url,null,token);
            if(response!=null) {
                ObservableList<Room> filteredList = FXCollections.observableArrayList();;
                JSONArray roomsArray = new JSONArray(response);
                for (int i = 0; i < roomsArray.length(); i++) {
                    JSONObject jsonObject = roomsArray.getJSONObject(i);
                    Room room = new Room(jsonObject.getInt("roomId"), jsonObject.getString("roomName"),
                            jsonObject.getString("roomType"), jsonObject.getString("roomNumber"),
                            jsonObject.getDouble("roomPricePerNight"), jsonObject.getString("roomAvailabilityStatus"),
                            jsonObject.getString("roomImage"));
                    filteredList.add(room);
                }
                showRooms(filteredList);
            }
            else{
                System.err.println("Error happened!");
            }
        }
        else {
            boolean isRoomNumber = false;
            for (int i = 0; i < searchBarText.length(); i++) {
                if (Character.isDigit(searchBarText.charAt(i))) {
                    isRoomNumber = true;
                    break;
                }
            }
            if (isRoomNumber) {
                String url ="http://localhost:8083/api/v1/rooms/roomNumber/" + searchBarText;
                String response = APi.executeAPI("GET",url,null,token);
                if(response!=null){
                    JSONObject jsonObject = new JSONObject(response);
                    Room room =new Room(jsonObject.getInt("roomId"), jsonObject.getString("roomName"),
                            jsonObject.getString("roomType"), jsonObject.getString("roomNumber"),
                            jsonObject.getDouble("roomPricePerNight"), jsonObject.getString("roomAvailabilityStatus"),
                            jsonObject.getString("roomImage"));
                            showFilteredRoom(room);
                }
                else{
                    System.err.println("Error happened!");
                }

            } else {
                String url = "http://localhost:8083/api/v1/rooms/roomName/" + URLEncoder.encode(searchBarText, StandardCharsets.UTF_8).replace("+", "%20");
                String response = APi.executeAPI("GET",url,null,token);
                if(response!=null){
                    JSONObject jsonObject = new JSONObject(response);
                    Room room =new Room(jsonObject.getInt("roomId"), jsonObject.getString("roomName"),
                            jsonObject.getString("roomType"), jsonObject.getString("roomNumber"),
                            jsonObject.getDouble("roomPricePerNight"), jsonObject.getString("roomAvailabilityStatus"),
                            jsonObject.getString("roomImage"));
                    showFilteredRoom(room);
                }
                else{
                    System.err.println("Error happened!");
                }
            }
        }
    }

    private void showFilteredRoom(Room r) {
        VBox card = getVboxCard(r, String.valueOf(r.getRoomId()));
        tilePane.getChildren().add(card);
        handleOnClickRoomCard(card, r);
    }

    public void showRooms(ObservableList<Room> rl) {
        tilePane.setPrefColumns(4);
        for (int i = 0; i < rl.size(); i++) {
            VBox card = getVboxCard(rl.get(i), String.valueOf(rl.get(i).getRoomId()));
            tilePane.getChildren().add(card);
            handleOnClickRoomCard(card, rl.get(i));
        }
    }

    private void setPane(Parent pane) {
        if (!parentStackPane.getChildren().isEmpty()) {
            parentStackPane.getChildren().clear();
        }
        parentStackPane.getChildren().add(pane);
    }

    private void handleOnClickRoomCard(VBox card, Room room) {
        card.setOnMouseClicked(e -> {
            FXMLLoader roomLoader = new FXMLLoader(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/room-page.fxml"));
            try {
                roomInfoPane = roomLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            roomPageController = roomLoader.getController();
            setPane(roomInfoPane);
            roomPageController.setRoomInfo(room);
        });
    }

    private VBox getVboxCard(Room r, String id) {
        String roomTopLabel = "Room " + r.getRoomNumber();
        String roomBottomLabel = r.getRoomName();
        VBox card = new VBox();
        card.setId(id);
        card.setPrefSize(200, 162);
        card.setStyle("-fx-background-color: #2196F3; " +
                "-fx-border-width: 5; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: white; " +
                "-fx-cursor: hand;");

        VBox topVBox = getTopVBox(roomTopLabel);
        VBox bottomVBox = getBottomVBox(roomBottomLabel);
        card.getChildren().addAll(topVBox, bottomVBox);
        return card;
    }

    private VBox getTopVBox(String topLabelString) {
        VBox topVbox = new VBox();
        topVbox.setPrefSize(165, 81);
        topVbox.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: white;");
        topVbox.setAlignment(Pos.CENTER);
        Label roomLabel = new Label(topLabelString);
        roomLabel.setTextFill(Color.WHITE);
        roomLabel.setFont(new Font(25));
        roomLabel.setWrapText(true);
        roomLabel.setMaxWidth(Double.MAX_VALUE);
        roomLabel.setAlignment(Pos.CENTER);
        topVbox.getChildren().add(roomLabel);
        return topVbox;
    }

    private VBox getBottomVBox(String bottomLabelString) {
        VBox bottomVbox = new VBox();
        bottomVbox.setPrefSize(165, 81);
        bottomVbox.setAlignment(Pos.CENTER);
        Label label = new Label(bottomLabelString);
        label.setTextFill(Color.WHITE);
        label.setFont(new Font(20));
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        bottomVbox.getChildren().add(label);
        return bottomVbox;
    }

    private void refreshPage(){
        if (!tilePane.getChildren().isEmpty()) {
            tilePane.getChildren().clear();
        }
        String url = "http://localhost:8083/api/v1/rooms";
        String response = APi.executeAPI("GET",url,null,token);
        if(response!=null) {
            ObservableList<Room> roomsList=FXCollections.observableArrayList();
            JSONArray allRoomsArray = new JSONArray(response);
            for (int i = 0; i < allRoomsArray.length(); i++) {
                JSONObject jsonObject = allRoomsArray.getJSONObject(i);
                Room room = new Room(jsonObject.getInt("roomId"), jsonObject.getString("roomName"),
                        jsonObject.getString("roomType"), jsonObject.getString("roomNumber"),
                        jsonObject.getDouble("roomPricePerNight"), jsonObject.getString("roomAvailabilityStatus"),
                        jsonObject.getString("roomImage"));
                roomsList.add(room);
            }
            showRooms(roomsList);
        }
        else{
            System.err.println("Error happened");
        }
    }

    private void loadButtonSound(){
        mediaPlayer = (new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/MediaPlayer/button_click.mp3")).toString())));
        mediaPlayer.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshPage();
    }
}

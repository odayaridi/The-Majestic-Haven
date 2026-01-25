package org.example.the_majestic_haven_midterm.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.the_majestic_haven_midterm.APiInterface.APi;
import org.example.the_majestic_haven_midterm.Models.Room;
import org.example.the_majestic_haven_midterm.Storage.TokenStorage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReportsController implements Initializable{

    @FXML
    private TableView<Room> availableRoomsTable;

    @FXML
    private VBox parentPane;

    @FXML
    private Button refreshButton;

    @FXML
    private ComboBox<?> reportComboBox;

    @FXML
    private BarChart<String,Number> hqrBarChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private CategoryAxis yAxis;

    @FXML
    private PieChart pieChart;

    @FXML
    private TableColumn<Room, String> roomName;

    @FXML
    private TableColumn<Room, String> roomNumber;

    @FXML
    private TableColumn<Room, Double> roomPrice;

    @FXML
    private TableColumn<Room, String> roomType;
    private MediaPlayer mediaPlayer;
    private final String token = TokenStorage.getToken();

    @FXML
    void onClickRefreshButton() {
        loadButtonSound();
        showAvailableRoomsThisWeek();
        showHighQualityRooms();
        showRoomTypesBookings();
    }

    private void showAvailableRoomsThisWeek(){
        String url = "http://localhost:8083/api/v1/rooms/availableRoomsWeek";
        String response = APi.executeAPI("GET",url,null,token);
        if(response!=null && !response.isEmpty()){
            ObservableList<Room> roomsAvailableList = FXCollections.observableArrayList();
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                roomsAvailableList.add(new Room(jsonObject.getInt("roomId"), jsonObject.getString("roomName"),
                        jsonObject.getString("roomType"), jsonObject.getString("roomNumber"),
                        jsonObject.getDouble("roomPricePerNight"), jsonObject.getString("roomAvailabilityStatus"),
                        jsonObject.getString("roomImage")));
            }
            roomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            roomName.setCellValueFactory(new PropertyValueFactory<>("roomName"));
            roomPrice.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));
            roomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
            availableRoomsTable.setItems(roomsAvailableList);
        }
        else{
            System.err.println("Error in fetching available rooms!");
        }
    }

    /*
 How to add bar chart in javafx from here:
    https://docs.oracle.com/javafx/2/charts/bar-chart.htm
  */
    public void showHighQualityRooms() {
        String url = "http://localhost:8083/api/v1/rooms/highQualityRooms";
        String response = APi.executeAPI("GET", url, null, token);

        if (response != null && !response.isEmpty()) {
            hqrBarChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("High Quality Rooms");
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String roomName = jsonObject.getString("roomName");
                double price = jsonObject.getDouble("pricePerNight");
                series.getData().add(new XYChart.Data<>(roomName, price));
            }
            hqrBarChart.getData().add(series);
        } else {
            System.err.println("Error in fetching high quality rooms!");
        }
    }


    /*
    How to add pie chart in javafx from here:
    http://docs.oracle.com/javafx/2/charts/pie-chart.htm
     */
    private void showRoomTypesBookings() {
        String url = "http://localhost:8083/api/v1/bookings/rTypeBookings";
        String response = APi.executeAPI("GET", url, null, token);
        if (response != null && !response.isEmpty()) {
            JSONArray jsonArray = new JSONArray(response);
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            double total = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                total += jsonArray.getJSONObject(i).getLong("totalBookings");
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type = jsonObject.getString("roomType");
                long bookings = jsonObject.getLong("totalBookings");
                double percentage = (bookings / total) * 100;
                String label = String.format("%s %.1f%%", type, percentage);
                pieChartData.add(new PieChart.Data(label, bookings));
            }
            pieChart.setData(pieChartData);
            pieChart.setLabelsVisible(false);
            pieChart.setLegendVisible(true);
        } else {
            System.err.println("Error happened while fetching total bookings of room types!");
        }
    }

    @FXML
    void onChooseReportType(ActionEvent event) {
        String value = (String) reportComboBox.getValue();
        if(value.equalsIgnoreCase("Available Rooms This Week")){
            showAvailableRoomsThisWeek();
            availableRoomsTable.setVisible(true);
            hqrBarChart.setVisible(false);
            pieChart.setVisible(false);
        } else if (value.equalsIgnoreCase("High Quality Rooms")) {
            showHighQualityRooms();
            availableRoomsTable.setVisible(false);
            pieChart.setVisible(false);
            hqrBarChart.setVisible(true);
        }
        else if (value.equalsIgnoreCase("Booked Room Types")){
            showRoomTypesBookings();
            availableRoomsTable.setVisible(false);
            pieChart.setVisible(true);
            hqrBarChart.setVisible(false);
        }

    }

    private void loadButtonSound(){
        mediaPlayer = (new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/MediaPlayer/button_click.mp3")).toString())));
        mediaPlayer.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAvailableRoomsThisWeek();
        showHighQualityRooms();
        showRoomTypesBookings();
    }
}

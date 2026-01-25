package org.example.the_majestic_haven_midterm.Controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.example.the_majestic_haven_midterm.Models.Client;
import org.example.the_majestic_haven_midterm.Singleton.ClientSession;
import org.example.the_majestic_haven_midterm.Singleton.SharedStackPane;
import org.example.the_majestic_haven_midterm.Storage.TokenStorage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ApplicationController implements Initializable {

    @FXML
    private FlowPane aboutUsPane;

    @FXML
    private FlowPane contactUsPane;

    @FXML
    private FlowPane editCredentialsPane;

    @FXML
    private Label clockTime;

    @FXML
    private FlowPane myBookingsPane;

    @FXML
    private ImageView logout;

    @FXML
    private StackPane parentPane;

    @FXML
    private FlowPane roomsPane;

    @FXML
    private FlowPane reportsPane;

    @FXML
    private Label time;

    @FXML
    private Text welcomeMessage;

    public Client client = ClientSession.getInstance().getCurrentUser();

    private final String token = TokenStorage.getToken();

    private FlowPane[] fPaneArray;

    private VBox contactUsPage,aboutUsPage,editCredentialsPage,roomsPage,myBookingsPage,clientStuckPage,reportsPage;

    @FXML
    void onClickAboutUsSection(MouseEvent event) {
        loadButtonSound();
        fPaneArray = new FlowPane[]{myBookingsPane, editCredentialsPane, contactUsPane, roomsPane,reportsPane};
        removeStyling(fPaneArray);
        applyStyling(aboutUsPane);
        setPane(aboutUsPage);
    }

    @FXML
    void onClickContactUsSection(MouseEvent event) {
        loadButtonSound();
        fPaneArray = new FlowPane[]{myBookingsPane, editCredentialsPane, aboutUsPane, roomsPane,reportsPane};
        removeStyling(fPaneArray);
        applyStyling(contactUsPane);
        setPane(contactUsPage);
    }

    @FXML
    void onClickEditCredentialsSection(MouseEvent event) {
        loadButtonSound();
        fPaneArray = new FlowPane[]{myBookingsPane, contactUsPane, aboutUsPane, roomsPane,reportsPane};
        removeStyling(fPaneArray);
        applyStyling(editCredentialsPane);
        setPane(editCredentialsPage);
    }

    @FXML
    void onClickReportsSection(MouseEvent event) {
        loadButtonSound();
        fPaneArray = new FlowPane[]{myBookingsPane, contactUsPane, aboutUsPane, roomsPane,editCredentialsPane};
        removeStyling(fPaneArray);
        applyStyling(reportsPane);
        setPane(reportsPage);
    }


    @FXML
    void onClickMyBookingsSection(MouseEvent event) {
        loadButtonSound();
        fPaneArray = new FlowPane[]{editCredentialsPane, contactUsPane, aboutUsPane, roomsPane,reportsPane};
        removeStyling(fPaneArray);
        applyStyling(myBookingsPane);
        setPane(myBookingsPage);
    }

    @FXML
    void onClickRoomsSection() {
        loadButtonSound();
        showRoomsSection();
    }

    @FXML
    void onClickLogout(MouseEvent event) throws IOException {
        loadButtonSound();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("This action will close the application and require you to re-login again!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ClientSession.getInstance().clearSession();
            TokenStorage.clearToken();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/main-page.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
            Scene scene = logout.getScene();
            Window window = scene.getWindow();
            ((Stage)window).close();
        }
    }

    @FXML
    void onClickStuck(MouseEvent event) {
        loadButtonSound();
        fPaneArray = new FlowPane[]{editCredentialsPane, contactUsPane, aboutUsPane, myBookingsPane,roomsPane,reportsPane};
        removeStyling(fPaneArray);
        setPane(clientStuckPage);
    }

    private void setPane(Parent pane) {
        if (!parentPane.getChildren().isEmpty()) {
            parentPane.getChildren().clear();
        }
        parentPane.getChildren().add(pane);
    }

    private void displayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        String s = formatter.format(date);
        time.setText(s);
    }

    private void applyStyling(FlowPane fPane) {
        fPane.setStyle("-fx-background-color: linear-gradient(to bottom, #0052CC, #2FA7FF);");
    }

    private void removeStyling(FlowPane[] fPaneArray) {
        for (int i = 0; i < fPaneArray.length; i++) {
            fPaneArray[i].setStyle(fPaneArray[i].getStyle()
                    .replace("-fx-background-color: linear-gradient(to bottom, #0052CC, #2FA7FF);", "")
                    + "-fx-cursor: hand;");
        }
    }

    private void colorAnimation(Text t) {
        Timeline colorAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(t.fillProperty(), Color.web("#FFFFFF"))),
                new KeyFrame(Duration.seconds(1.25), new KeyValue(t.fillProperty(), Color.web("#0052CC")))
        );
        colorAnimation.setAutoReverse(true);
        colorAnimation.setCycleCount(Animation.INDEFINITE);
        colorAnimation.play();
    }

    private void setWelcomeMessageText(Text t) {
        if (client != null) {
            String firstName = client.getFirstName();
            String formattedName = "";
            if (firstName.length() > 0) {
                formattedName = Character.toUpperCase(firstName.charAt(0)) + "";
            }
            for (int i = 1; i < firstName.length(); i++) {
                formattedName += Character.toLowerCase(firstName.charAt(i));
            }
            t.setText("Welcome " + formattedName);
        }

    }

    private void setFxmLoaderInterface() {
        try {
            roomsPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/room-section.fxml")));
            reportsPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/reports-section.fxml")));
            editCredentialsPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/edit-credentials.fxml")));
            contactUsPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/contact-us.fxml")));
            aboutUsPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/about-us.fxml")));
            myBookingsPage =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/my-bookings.fxml")));
            clientStuckPage =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/Views/client-stuck.fxml")));
            showRoomsSection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setClockTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Calendar cal = Calendar.getInstance();
            int second = cal.get(Calendar.SECOND);
            int minute = cal.get(Calendar.MINUTE);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            clockTime.setText(String.format("%02d:%02d:%02d", hour, minute, second));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void loadButtonSound(){
        MediaPlayer mediaPlayer = (new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/MediaPlayer/button_click.mp3")).toString())));
        mediaPlayer.play();
    }

    private void showRoomsSection(){
        fPaneArray = new FlowPane[]{editCredentialsPane, contactUsPane, aboutUsPane, myBookingsPane,reportsPane};
        removeStyling(fPaneArray);
        applyStyling(roomsPane);
        setPane(roomsPage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SharedStackPane.getInstance().setParentStackPane(parentPane);
        setFxmLoaderInterface();
        setWelcomeMessageText(welcomeMessage);
        colorAnimation(welcomeMessage);
        setClockTime();
        displayDate();
    }

   }

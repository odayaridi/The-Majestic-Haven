package org.example.the_majestic_haven_midterm.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientStuckController implements Initializable {
    @FXML
    private MediaView videoGuide;

    private MediaPlayer buttonSoundPlayer;

    private MediaPlayer mediaPlayer;

    @FXML
    void onClickPauseButton(ActionEvent event) {
        loadButtonSound();
        mediaPlayer.pause();
    }

    @FXML
    void onClickPlayButton(ActionEvent event) {
        loadButtonSound();
        mediaPlayer.play();
    }

    @FXML
    void onClickResetButton(ActionEvent event) {
        loadButtonSound();
        if (mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
    }

    private void loadButtonSound() {
        buttonSoundPlayer = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/org/example/the_majestic_haven_midterm/MediaPlayer/button_click.mp3")).toString()));
        buttonSoundPlayer.play();
    }

    private void showVideo() {
        String videoPath = getClass().getResource("/org/example/the_majestic_haven_midterm/Video/video_guide.mp4").toExternalForm();
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        videoGuide.setMediaPlayer(mediaPlayer);
        videoGuide.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (oldScene != null && newScene == null) {
                mediaPlayer.stop();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showVideo();
    }
}

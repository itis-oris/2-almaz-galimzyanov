package ru.itis.fisd.client.gui.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import lombok.Getter;

import java.util.Random;

public class EndGameFXController {

    @Getter
    private static EndGameFXController instance;

    public EndGameFXController() {
        instance = this;
    }

    @FXML
    private Label winlost;

    @FXML
    private Button back;

    @FXML
    private Pane animationPane;

    @FXML
    public void initialize() {

        back.setOnAction(_ -> SceneController.activate("start"));
    }

    public void setWin(int player, int order) {
        boolean isWin = (player == order);
        winlost.setText(isWin ? "WIN" : "LOSE");
        startFallingCircles(isWin ? Color.GREEN : Color.RED);
    }

    private void startFallingCircles(Color color) {
        Random random = new Random();

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.1), _ -> {
            Circle circle = new Circle(10, color);
            circle.setLayoutX(random.nextDouble() * animationPane.getWidth());
            circle.setLayoutY(-20);
            animationPane.getChildren().add(circle);

            TranslateTransition fall = new TranslateTransition(Duration.seconds(2), circle);
            fall.setToY(animationPane.getHeight() + 20);
            fall.setOnFinished(_ -> animationPane.getChildren().remove(circle));
            fall.play();
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}

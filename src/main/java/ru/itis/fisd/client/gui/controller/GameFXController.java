package ru.itis.fisd.client.gui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;
import ru.itis.fisd.entity.Card;
import ru.itis.fisd.entity.Player;
import ru.itis.fisd.listener.GameActionListener;

import java.util.List;

public class GameFXController {

    @Setter
    Player player;

    @Getter
    private static GameFXController instance;

    public GameFXController() {
        instance = this;
    }

    @FXML
    public Button uno;

    @FXML
    public Label enemy;

    @FXML
    public Label cards;

    @Getter
    @FXML
    private HBox buttonRow;

    @FXML
    private Button deck;

    @FXML
    private Label current;

    private static GameActionListener buttonActionListener;


    @FXML
    public void initialize() {
        initStyle();

        buttonActionListener = new GameActionListener(this);
    }

    private void initStyle() {
        deck.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        current.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        current.setFont(Font.font("Arial", FontWeight.BOLD, 128));
        current.setStyle("-fx-font-size: 400%;");
        current.setTextFill(Color.WHITE);
        current.setPrefWidth(80);
        current.setPrefHeight(150);
    }

    private static Button getButton(Card card, Player player) {
        Button button = new Button(String.valueOf(card.value()));

        button.setFont(Font.font("Arial", FontWeight.BOLD, 128));
        button.setTextFill(Color.WHITE);

        button.setStyle("-fx-font-size: 500%;");

        button.setPrefWidth(80);
        button.setPrefHeight(150);
        BackgroundFill background_fill = new BackgroundFill(card.color().getColor(), CornerRadii.EMPTY, Insets.EMPTY);

        button.setBackground(new Background(background_fill));

        button.setOnAction(_ -> {
            System.out.println("Нажата кнопка: " + button.getText());
            buttonActionListener.handleButton(button, player);
        });
        return button;
    }

    public void updatePlayerCards(List<Card> playerCards) {
        buttonRow.getChildren().clear();
        for (Card card : playerCards) {
            Button button = getButton(card, player);
            buttonRow.getChildren().add(button);
        }
    }

    public void setDeckCard(String value, Paint color) {

        if (current != null) {
            Platform.runLater(() -> {
                current.setText(value);
                current.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            });
        }
    }

    public void removeCard(Card card) {
        Platform.runLater(() -> buttonRow.getChildren().removeIf(node -> {
            if (node instanceof Button button) {
                String buttonValue = button.getText();
                Paint buttonColor = button.getBackground().getFills().getFirst().getFill();
                return buttonValue.equals(String.valueOf(card.value())) && buttonColor.equals(card.color().getColor());
            }
            return false;
        }));
    }

}

package ru.itis.fisd.client.gui.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Random;

public class GameFXController {

    @FXML
    private HBox buttonRow; // Контейнер для строки с кнопками

    @FXML
    public void initialize() {
        int totalButtons = 10; // Пример: 50 кнопок

        for (int i = 0; i < totalButtons; i++) {
            // Создаем кнопку
            Button button = new Button("Button " + (i + 1));
            button.setPrefWidth(100); // Ширина кнопки
            button.setPrefHeight(50); // Высота кнопки
            Color[] colors = {Color.PINK, Color.AQUAMARINE, Color.GOLD};
            Random rand = new Random();

            BackgroundFill background_fill = new BackgroundFill(colors[rand.nextInt(colors.length)], CornerRadii.EMPTY, Insets.EMPTY);

            // create Background
            button.setBackground(new Background(background_fill));

            // Обработчик нажатия
            button.setOnAction(_ -> System.out.println("Нажата кнопка: " + button.getText()));

            // Добавляем кнопку в строку
            buttonRow.getChildren().add(button);
        }
    }
}

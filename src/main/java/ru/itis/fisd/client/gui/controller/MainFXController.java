package ru.itis.fisd.client.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ru.itis.fisd.listener.ButtonActionListener;

public class MainFXController {

    @FXML
    private Button register;

    @FXML
    private Button history;

    @FXML
    private Button startGame;

    private ButtonActionListener buttonActionListener;

    @FXML
    public void initialize() {
        buttonActionListener = new ButtonActionListener();

        register.setOnAction(_ -> buttonActionListener.handleButtonAction(register));
        history.setOnAction(_ -> buttonActionListener.handleButtonAction(history));
        startGame.setOnAction(_ -> buttonActionListener.handleButtonAction(startGame));
    }

}

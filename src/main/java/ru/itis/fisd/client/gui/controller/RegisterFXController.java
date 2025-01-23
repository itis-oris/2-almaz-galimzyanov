package ru.itis.fisd.client.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.itis.fisd.listener.ButtonActionListener;

public class RegisterFXController {

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerUser;

    @FXML
    private Button cancelButton;

    @FXML
    private Button infoButton;

    private ButtonActionListener buttonActionListener;

    @FXML
    public void initialize() {
        buttonActionListener = new ButtonActionListener(nameField, passwordField);

        registerUser.setOnAction(_ -> buttonActionListener.handleButtonAction(registerUser));
        cancelButton.setOnAction(_ -> buttonActionListener.handleButtonAction(cancelButton));
        infoButton.setOnAction(_ -> buttonActionListener.handleButtonAction(infoButton));
    }
}

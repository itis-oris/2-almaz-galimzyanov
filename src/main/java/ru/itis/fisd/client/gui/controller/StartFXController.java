package ru.itis.fisd.client.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.itis.fisd.listener.ButtonActionListener;

public class StartFXController {

    @FXML
    private TextField port;

    @FXML
    private Button start;

    private ButtonActionListener buttonActionListener;

    @FXML
    public void initialize() {
        buttonActionListener = new ButtonActionListener();
        System.out.println(port);
        start.setOnAction(_ -> {
            buttonActionListener.setPort(Integer.parseInt(port.getText()));
            buttonActionListener.handleButtonAction(start);
        });
    }
}

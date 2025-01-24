package ru.itis.fisd.client.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.itis.fisd.listener.ButtonActionListener;

public class StatisticsFXController {

    @FXML
    private Label all;

    @FXML
    private Label win;

    @FXML
    private Label percent;

    @FXML
    private Button back;

    private ButtonActionListener buttonActionListener;

    @FXML
    public void initialize() {
        buttonActionListener = new ButtonActionListener();

        back.setOnAction(_ -> buttonActionListener.handleButtonAction(back));
    }
}

package ru.itis.fisd.client.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.itis.fisd.listener.ButtonActionListener;
import ru.itis.fisd.model.AccountEntity;
import ru.itis.fisd.service.AccountService;

import java.util.Optional;

public class LoginFxController {

    private static final AccountService service = new AccountService();

    @FXML
    public Label error;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button login;

    @FXML
    private Button cancelButton;


    private ButtonActionListener buttonActionListener;

    @FXML
    public void initialize() {
        buttonActionListener = new ButtonActionListener(nameField, passwordField);

        login.setOnAction(_ -> {
            if (checkUser()) {
                buttonActionListener.handleButtonAction(login);
            } else {
                error.setText("Username or password is incorrect");
                error.setVisible(true);

            }
        });
        cancelButton.setOnAction(_ -> buttonActionListener.handleButtonAction(cancelButton));
    }

    private boolean checkUser() {
        Optional<AccountEntity> account = service.getByName(nameField.getText());
        System.out.println(account);
        if (account.isPresent()) {
            BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
            String password = passwordField.getText();
            return bCrypt.matches(password, account.get().getPassword());
        }
        return false;
    }
}

package ru.itis.fisd.listener;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.itis.fisd.client.gui.controller.SceneController;
import ru.itis.fisd.model.AccountEntity;
import ru.itis.fisd.service.AccountService;

@AllArgsConstructor
@NoArgsConstructor
public class ButtonActionListener {

    private final AccountService userService = new AccountService();
    private TextField username;
    private PasswordField passwordField;

    public void handleButtonAction(Button button) {
        String text = button.getText();
        switch (text) {
            case "Add User" -> {
                if (username.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "Please fill all the fields", "Info");
                } else {
                    BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
                    AccountEntity user = AccountEntity
                            .builder()
                            .name(username.getText())
                            .password(bCrypt.encode(passwordField.getText()))
                            .build();
                    username.clear();
                    passwordField.clear();
                    try {
                        boolean result = userService.save(user);

                        if (!result) {
                            showAlert(Alert.AlertType.ERROR, "User not added", "Error");
                        }
                    } catch (RuntimeException ex) {
                        showAlert(Alert.AlertType.WARNING, ex.getMessage(), "Warning");
                    }
                }
            }
            case "Cancel" -> {
                username.clear();
                passwordField.clear();
            }
            case "Info" -> showAlert(
                    Alert.AlertType.INFORMATION,
                    """
                            Это форма для добавления нового пользователя.
                            Введите имя пользователя и пароль, затем нажмите Register.
                            Чтобы очистить поля, нажмите Cancel.
                            """,
                    "Справка"
            );

            case "Register" -> SceneController.activate("register");
        }
    }

    private void showAlert(Alert.AlertType alertType, String content, String title) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
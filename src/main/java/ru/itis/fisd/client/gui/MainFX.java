package ru.itis.fisd.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.itis.fisd.client.Client;
import ru.itis.fisd.client.gui.controller.SceneController;
import ru.itis.fisd.database.DBConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

@Setter
@Getter
public class MainFX extends Application {

    private Socket clientSocket;
    private Client client;
    private static Stage myStage;

    @Override
    public void start(Stage stage) {
        DBConnection.getInstance();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 460, 480);
            SceneController controller = new SceneController(scene);
            addScenes(controller);

            stage.setTitle("UNO Game");
            stage.setScene(scene);
            myStage = stage;

            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void addScenes(SceneController controller) {
        try {
            controller.addScene("main", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/main.fxml"))));
            controller.addScene("register", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/register.fxml"))));
            controller.addScene("login", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login.fxml"))));
            controller.addScene("start", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/start.fxml"))));
            controller.addScene("waiting_room", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/waiting_room.fxml"))));
            controller.addScene("game", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/game.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void todo() {
        myStage.setOnCloseRequest(_ -> {
            if (clientSocket != null && !clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DBConnection.getInstance().destroy();
            }
            System.exit(0);
        });
    }
}

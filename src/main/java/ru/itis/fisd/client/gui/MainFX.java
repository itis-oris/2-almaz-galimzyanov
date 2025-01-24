package ru.itis.fisd.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.itis.fisd.client.gui.controller.SceneController;
import ru.itis.fisd.database.DBConnection;

import java.io.IOException;
import java.util.Objects;

public class MainFX extends Application {

//    @Setter
//    private static Socket clientSocket;

    @Override
    public void start(Stage stage) {
        DBConnection.getInstance();

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
            Parent root = loader.load();

            // Setup the scene
            Scene scene = new Scene(root, 460, 300);
            SceneController controller = new SceneController(scene);
            addScenes(controller);

            stage.setTitle("UNO Game");
            stage.setScene(scene);

            // Close resources on exit
            stage.setOnCloseRequest(event -> {
                //                    if (clientSocket != null && !clientSocket.isClosed()) {
//                        clientSocket.close();
                DBConnection.getInstance().destroy();
                System.out.println("Connection closed by MainFX.");
//                    }
            });

            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addScenes(SceneController controller) {
        try {
            controller.addScene("main", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/main.fxml"))));
            controller.addScene("register", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/register.fxml"))));
            controller.addScene("login", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login.fxml"))));
            controller.addScene("start", FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/start.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

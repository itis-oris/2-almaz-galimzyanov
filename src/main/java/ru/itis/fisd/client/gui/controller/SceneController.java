package ru.itis.fisd.client.gui.controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class SceneController {

    private static final HashMap<String, Pane> screenMap = new HashMap<>();
    private static Scene main;

    public SceneController(Scene main) {
        SceneController.main = main;
    }

    public void addScene(String name, Pane pane){
        screenMap.put(name, pane);
    }

    public void removeScene(String name){
        screenMap.remove(name);
    }

    public static void activate(String name){
        main.setRoot(screenMap.get(name));
    }
}

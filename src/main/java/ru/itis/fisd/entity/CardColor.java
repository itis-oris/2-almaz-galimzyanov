package ru.itis.fisd.entity;


import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public enum CardColor {

    RED(Color.RED), GREEN(Color.GREEN), BLUE(Color.BLUE), YELLOW(Color.YELLOW);

    private final Color color;

    CardColor(Color color) {
        this.color = color;
    }
}

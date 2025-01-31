package ru.itis.fisd.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Player {

    private List<Card> playerCards = new ArrayList<>();
    private boolean isUno = false;

}

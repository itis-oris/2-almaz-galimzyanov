package ru.itis.fisd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameHistoryEntity {
    private Long id;
    private Long playerFirstId;
    private Long playerSecondId;
    private Long playerWinId;
}

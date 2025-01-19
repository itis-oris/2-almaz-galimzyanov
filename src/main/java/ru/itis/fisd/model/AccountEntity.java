package ru.itis.fisd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountEntity {
    private Long id;
    private String name;
    private String password;
    private Integer allGames;
    private Integer winGames;
}

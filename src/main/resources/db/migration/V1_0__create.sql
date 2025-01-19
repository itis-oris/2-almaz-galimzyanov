CREATE TABLE IF NOT EXISTS account
(
    id       BIGSERIAL,
    name     VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    all_games INT NOT NULL,
    win_games INT NOT NULL,
    --------------------------------------
    CONSTRAINT name_uq UNIQUE (name),
    CONSTRAINT account_id_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS  game_history
(
    id               BIGSERIAL,
    player_first_id  BIGSERIAL,
    player_second_id BIGSERIAL,
    player_win_id    BIGSERIAL,
    ------------------------------------------------------------------------------------
    CONSTRAINT game_history_id_pk PRIMARY KEY (id),
    CONSTRAINT player_first_id_fk FOREIGN KEY (player_first_id) REFERENCES account (id),
    CONSTRAINT player_second_id_fk FOREIGN KEY (player_second_id) REFERENCES account (id),
    CONSTRAINT player_win_id_fk FOREIGN KEY (player_win_id) REFERENCES account (id)
);
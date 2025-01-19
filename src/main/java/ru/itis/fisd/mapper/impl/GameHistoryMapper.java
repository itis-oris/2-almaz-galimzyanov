package ru.itis.fisd.mapper.impl;

import ru.itis.fisd.mapper.api.RowMapper;
import ru.itis.fisd.model.GameHistoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameHistoryMapper implements RowMapper<GameHistoryEntity> {
    @Override
    public GameHistoryEntity mapRow(ResultSet resultSet) throws SQLException {
        return new GameHistoryEntity(
                resultSet.getLong("id"),
                resultSet.getLong("player_first_id"),
                resultSet.getLong("player_second_id"),
                resultSet.getLong("player_win_id")
        );
    }
}

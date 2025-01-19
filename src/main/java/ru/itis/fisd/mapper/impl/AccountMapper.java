package ru.itis.fisd.mapper.impl;

import ru.itis.fisd.mapper.api.RowMapper;
import ru.itis.fisd.model.AccountEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements RowMapper<AccountEntity> {
    @Override
    public AccountEntity mapRow(ResultSet resultSet) throws SQLException {
        return new AccountEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("password"),
                resultSet.getInt("all_games"),
                resultSet.getInt("win_games")
        );
    }
}

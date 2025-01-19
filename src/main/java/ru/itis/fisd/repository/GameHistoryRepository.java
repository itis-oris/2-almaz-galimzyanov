package ru.itis.fisd.repository;

import lombok.RequiredArgsConstructor;
import ru.itis.fisd.database.DBConnection;
import ru.itis.fisd.mapper.api.RowMapper;
import ru.itis.fisd.model.GameHistoryEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GameHistoryRepository {

    private final DBConnection db = DBConnection.getInstance();

    private final RowMapper<GameHistoryEntity> mapper;

    /* language=sql */
    private static final String SQL_GET_ALL = """
            SELECT *
            FROM game_history
            """;

    private static final String SQL_INSERT = """
            INSERT INTO game_history(player_first_id, player_second_id, player_win_id)
            VALUES (?, ?, ?)
            """;

    public List<GameHistoryEntity> getAll() {
        try (
                Connection connection = db.getConnection()
        ) {
            PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL);
            ResultSet result = statement.executeQuery();

            db.releaseConnection(connection);

            List<GameHistoryEntity> gameHistoryEntities = new ArrayList<>();
            while (result.next()) {
                gameHistoryEntities.add(mapper.mapRow(result));
            }

            return gameHistoryEntities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(GameHistoryEntity entity) {
        try (
                Connection connection = db.getConnection()
        ) {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
            statement.setLong(1, entity.getPlayerFirstId());
            statement.setLong(2, entity.getPlayerSecondId());
            statement.setLong(3, entity.getPlayerWinId());

            int result = statement.executeUpdate();
            db.releaseConnection(connection);
            return result == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

package ru.itis.fisd.repository;

import lombok.RequiredArgsConstructor;
import ru.itis.fisd.database.DBConnection;
import ru.itis.fisd.mapper.api.RowMapper;
import ru.itis.fisd.model.AccountEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class AccountRepository {

    private final DBConnection db = DBConnection.getInstance();

    private final RowMapper<AccountEntity> mapper;

    /* language=sql */
    private static final String SQL_GET_BY_ID = """
            SELECT *
            FROM account
            WHERE id = ?
            """;

    private static final String SQL_INSERT = """
            INSERT INTO account(name, password, all_games, win_games)
            VALUES (?, ?, ?, ?)
            """;

    private static final String SQL_UPDATE = """
            UPDATE account
            SET name = ?, password = ?
            WHERE id = ?
            """;

    private static final String SQL_UPDATE_INFO = """
            UPDATE account
            SET all_games = ?, win_games = ?
            WHERE id = ?
            """;


    public Optional<AccountEntity> getById(Long id) {
        try (
                Connection connection = db.getConnection()
        ) {
            PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ID);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            db.releaseConnection(connection);

            return result.next() ? Optional.of(mapper.mapRow(result)) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(AccountEntity entity) {
        try (
                Connection connection = db.getConnection()
        ) {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPassword());
            statement.setInt(3, 0);
            statement.setInt(4, 0);

            int result = statement.executeUpdate();
            db.releaseConnection(connection);
            return result == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Account with this name already exists");
        }
    }

    public boolean update(AccountEntity entity) {
        try (
                Connection connection = db.getConnection()
        ) {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPassword());
            statement.setLong(3, entity.getId());

            int result = statement.executeUpdate();
            db.releaseConnection(connection);
            return result == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateInfo(AccountEntity entity) {
        try (
                Connection connection = db.getConnection()
        ) {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_INFO);
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getAllGames());
            statement.setInt(3, entity.getWinGames());

            int result = statement.executeUpdate();
            db.releaseConnection(connection);
            return result == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

package ru.itis.fisd.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/semWork2";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    private static HikariDataSource dataSource;

    private DBConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(DB_URL);
            config.setUsername(DB_USER);
            config.setPassword(DB_PASSWORD);
            config.setConnectionTimeout(50000);
            config.setMaximumPoolSize(10);

            dataSource = new HikariDataSource(config);

            Flyway flyway = Flyway.configure().dataSource(dataSource).load();
            System.out.println("start migration");
            flyway.migrate();
            System.out.println("migration done");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void releaseConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public void destroy() {
        dataSource.close();
    }
}

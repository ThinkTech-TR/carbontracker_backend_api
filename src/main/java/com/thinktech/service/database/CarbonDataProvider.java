package com.thinktech.service.database;

import java.sql.*;

public abstract class CarbonDataProvider {
    protected Connection connection = null;
    protected PreparedStatement preparedStatement = null;
    protected CallableStatement callableStatement = null;
    protected ResultSet resultSet = null;

    protected Connection OpenConnection() throws SQLException {
        String connectionString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
                System.getenv("DB_HOST"),
                System.getenv("DB_NAME"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD"));
        return DriverManager.getConnection(connectionString);
    }

    protected void CloseConnection() throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (callableStatement != null) {
            callableStatement.close();
        }

        if (connection != null) {
            connection.close();
        }
    }


}

package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;
    public UserDaoJDBCImpl() {
        this.connection = Util.getConnection();
    }

    public void createUsersTable() {
        String query =
                """
                        CREATE TABLE IF NOT EXISTS `mydbtest`.`users` (
                          `id` INT NOT NULL AUTO_INCREMENT,
                          `name` VARCHAR(45) NULL,
                          `lastname` VARCHAR(45) NULL,
                          `age` INT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);""";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String query =
                "DROP TABLE IF EXISTS `mydbtest`.`users`;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `mydbtest`.`users`(name, lastname, age) VALUE(?,?,?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `mydbtest`.`users` WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM `mydbtest`.`users`;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getByte("age")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        String query = "TRUNCATE `mydbtest`.`users`;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

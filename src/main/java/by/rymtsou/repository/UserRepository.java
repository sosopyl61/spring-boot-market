package by.rymtsou.repository;

import by.rymtsou.config.DatabaseConfig;
import by.rymtsou.config.SQLQuery;
import by.rymtsou.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final DatabaseConfig databaseConfig;

    @Autowired
    public UserRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public Optional<User> getUserById(Long id) {
        Connection connection = databaseConfig.getConnection();

        try {
            PreparedStatement getUserStatement = connection.prepareStatement(SQLQuery.GET_USER_BY_ID);
            getUserStatement.setLong(1, id);
            ResultSet resultSet = getUserStatement.executeQuery();
            return parseUser(resultSet);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<Long> createUser(User user) {
        Long userId = null;
        try (Connection connection = databaseConfig.getConnection()) {
            PreparedStatement createUserStatement = connection.prepareStatement(SQLQuery.CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            createUserStatement.setString(1, user.getFirstname());
            createUserStatement.setString(2, user.getSecondName());
            createUserStatement.setInt(3, user.getAge());
            createUserStatement.setString(4, user.getTelephoneNumber());
            createUserStatement.setString(5, user.getEmail());
            createUserStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            createUserStatement.setString(7, user.getSex());
            createUserStatement.setBoolean(8, false);

            ResultSet resultSet = createUserStatement.getGeneratedKeys();
            if (resultSet.next()) {
                userId = resultSet.getLong(1);
            }
            return Optional.of(userId);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Boolean updateUser(User user) {
        Connection connection = databaseConfig.getConnection();
        try {
            PreparedStatement updateUserStatement = connection.prepareStatement(SQLQuery.UPDATE_USER);
            updateUserStatement.setString(1, user.getFirstname());
            updateUserStatement.setString(2, user.getSecondName());
            updateUserStatement.setInt(3, user.getAge());
            updateUserStatement.setString(4, user.getTelephoneNumber());
            updateUserStatement.setString(5, user.getEmail());
            updateUserStatement.setString(6, user.getSex());
            updateUserStatement.setLong(7, user.getId());
            return updateUserStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean deleteUser(Long id) {
        Connection connection = databaseConfig.getConnection();
        try {
            PreparedStatement deleteUserStatement = connection.prepareStatement(SQLQuery.DELETE_USER);
            deleteUserStatement.setLong(1, id);
            return deleteUserStatement.execute();
        } catch (SQLException e) {
            return false;
        }
    }

    private Optional<User> parseUser (ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setFirstname(resultSet.getString("firstname"));
            user.setSecondName(resultSet.getString("second_name"));
            user.setAge(resultSet.getInt("age"));
            user.setTelephoneNumber(resultSet.getString("telephone_number"));
            user.setEmail(resultSet.getString("email"));
            user.setCreated(resultSet.getTimestamp("created"));
            user.setUpdated(resultSet.getTimestamp("updated"));
            user.setSex(resultSet.getString("sex"));
            user.setDeleted(resultSet.getBoolean("deleted"));
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection()) {
            PreparedStatement getUsersStatement = connection.prepareStatement(SQLQuery.GET_ALL_USERS);
            ResultSet resultSet = getUsersStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setSecondName(resultSet.getString("second_name"));
                user.setAge(resultSet.getInt("age"));
                user.setTelephoneNumber(resultSet.getString("telephone_number"));
                user.setEmail(resultSet.getString("email"));
                user.setCreated(resultSet.getTimestamp("created"));
                user.setUpdated(resultSet.getTimestamp("updated"));
                user.setSex(resultSet.getString("sex"));
                user.setDeleted(resultSet.getBoolean("deleted"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }
}

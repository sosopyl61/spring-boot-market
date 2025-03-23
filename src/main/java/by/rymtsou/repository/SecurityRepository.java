package by.rymtsou.repository;

import by.rymtsou.config.DatabaseConfig;
import by.rymtsou.config.SQLQuery;
import by.rymtsou.model.Security;
import by.rymtsou.model.User;
import by.rymtsou.model.dto.RegistrationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class SecurityRepository {

    private final DatabaseConfig databaseConfig;

    @Autowired
    public SecurityRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public Optional<Security> getSecurityById(Long id) {
        Connection connection = databaseConfig.getConnection();

        try {
            PreparedStatement getSecurityStatement = connection.prepareStatement(SQLQuery.GET_SECURITY_BY_ID);
            getSecurityStatement.setLong(1, id);

            ResultSet resultSet = getSecurityStatement.executeQuery();
            return parseSecurity(resultSet);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<User> registration(RegistrationRequestDto registrationRequestDto) throws SQLException {
        Connection connection = databaseConfig.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement createUserStatement = connection.prepareStatement(SQLQuery.CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            createUserStatement.setString(1, registrationRequestDto.getFirstname());
            createUserStatement.setString(2, registrationRequestDto.getSecondName());
            createUserStatement.setInt(3, registrationRequestDto.getAge());
            createUserStatement.setString(4, registrationRequestDto.getTelephoneNumber());
            createUserStatement.setString(5, registrationRequestDto.getEmail());
            createUserStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            createUserStatement.setString(7, registrationRequestDto.getSex());
            createUserStatement.setBoolean(8, false);
            createUserStatement.executeUpdate();

            ResultSet generatedKeys = createUserStatement.getGeneratedKeys();
            long userId = -1;
            if (generatedKeys.next()) {
                userId = generatedKeys.getLong(1);
            }

            PreparedStatement createSecurityStatement = connection.prepareStatement(SQLQuery.CREATE_SECURITY);
            createSecurityStatement.setString(1, registrationRequestDto.getLogin());
            createSecurityStatement.setString(2, registrationRequestDto.getPassword());
            createSecurityStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            createSecurityStatement.setLong(4, userId);
            createSecurityStatement.executeUpdate();
            connection.commit();
            return Optional.of(new User(
                    userId,
                    registrationRequestDto.getFirstname(),
                    registrationRequestDto.getSecondName(),
                    registrationRequestDto.getAge(),
                    registrationRequestDto.getEmail(),
                    registrationRequestDto.getSex(),
                    registrationRequestDto.getTelephoneNumber(),
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()),
                    false, null)
            );
        } catch (SQLException e) {
            connection.rollback();
        }
        return Optional.empty();
    }

    public Boolean updateSecurity(Security security) {
        try (Connection connection = databaseConfig.getConnection()) {
            PreparedStatement updateSecurityStatement = connection.prepareStatement(SQLQuery.UPDATE_SECURITY);
            updateSecurityStatement.setString(1, security.getLogin());
            updateSecurityStatement.setString(2, security.getPassword());
            updateSecurityStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            updateSecurityStatement.setLong(4, security.getId());

            return updateSecurityStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while updating security: " + e.getMessage());
        }
        return false;
    }

    public Boolean deleteSecurity(Long id) {
        try (Connection connection = databaseConfig.getConnection()) {
            PreparedStatement deleteSecurityStatement = connection.prepareStatement(SQLQuery.DELETE_SECURITY);
            deleteSecurityStatement.setLong(1, id);
            return deleteSecurityStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while deleting security: " + e.getMessage());
        }
        return false;
    }

    private Optional<Security> parseSecurity (ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Security security = new Security();
            security.setId(resultSet.getLong("id"));
            security.setLogin(resultSet.getString("login"));
            security.setPassword(resultSet.getString("password"));
            security.setCreated(resultSet.getTimestamp("created"));
            security.setUpdated(resultSet.getTimestamp("updated"));
            security.setUserId(resultSet.getLong("user_id"));
            return Optional.of(security);
        }
        return Optional.empty();
    }
}

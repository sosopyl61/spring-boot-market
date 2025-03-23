package by.rymtsou.repository;

import by.rymtsou.config.DatabaseConfig;
import by.rymtsou.config.SQLQuery;
import by.rymtsou.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final DatabaseConfig databaseConfig;

    @Autowired
    public ProductRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public Optional<Product> getProductById(Long id) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQLQuery.GET_PRODUCT_BY_ID)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return parseProduct(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error while finding product: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = databaseConfig.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQLQuery.GET_ALL_PRODUCTS);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                product.setCreated(resultSet.getTimestamp("created"));
                product.setUpdated(resultSet.getTimestamp("updated"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error finding all products: " + e.getMessage());
        }
        return products;
    }

    public Optional<Product> createProduct(Product product) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQLQuery.CREATE_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setTimestamp(4, null);
            statement.executeUpdate();

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                product.setId(resultSet.getLong(1));
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating product: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Boolean updateProduct(Product product) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQLQuery.UPDATE_PRODUCT)) {

            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setLong(4, product.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while updating product: " + e.getMessage());
        }
        return false;
    }

    public Boolean deleteProduct(Long id) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQLQuery.DELETE_PRODUCT)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while deleting product: " + e.getMessage());
        }
        return false;
    }

    private Optional<Product> parseProduct(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Product product = new Product();
            product.setId(resultSet.getLong("id"));
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getDouble("price"));
            product.setCreated(resultSet.getTimestamp("created"));
            product.setUpdated(resultSet.getTimestamp("updated"));
            return Optional.of(product);
        }
        return Optional.empty();
    }
}

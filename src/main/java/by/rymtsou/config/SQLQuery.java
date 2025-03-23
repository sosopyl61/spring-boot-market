package by.rymtsou.config;

public interface SQLQuery {
    String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    String GET_ALL_USERS = "SELECT * FROM users WHERE is_deleted = false";
    String CREATE_USER = "INSERT INTO users (id, firstname, second_name, age, telephone_number, email, created, updated, sex, deleted) " +
            "VALUES (DEFAULT, ?, ?, ?, ?, ?, DEFAULT, ?, ?, ?)";
    String UPDATE_USER = "UPDATE users SET firstname=?, second_name=?, age=?, telephone_number=?, email=?, sex=?, updated=DEFAULT WHERE id = ?";
    String DELETE_USER = "DELETE from users WHERE id = ?";

    String GET_SECURITY_BY_ID = "SELECT * FROM security WHERE id = ?";
    String CREATE_SECURITY = "  INSERT INTO security (id, login, password, created, updated, user_id) " +
            "VALUES (DEFAULT, ?, ?, DEFAULT, ?, ?)";
    String UPDATE_SECURITY = "UPDATE security SET login = ?, password = ?, updated = DEFAULT WHERE id = ?";
    String DELETE_SECURITY = "UPDATE security SET login = NULL, password = NULL, updated = DEFAULT WHERE id = ?";

    String GET_PRODUCT_BY_ID = "SELECT * FROM product WHERE id = ?";
    String CREATE_PRODUCT = "INSERT INTO products (id, name, price, created, updated) VALUES (DEFAULT, ?, ?, DEFAULT, ?)";
    String UPDATE_PRODUCT = "UPDATE products SET name = ?, price = ?, updated = DEFAULT WHERE id = ?";
    String DELETE_PRODUCT = "DELETE FROM product WHERE id = ?";
    String GET_ALL_PRODUCTS = "SELECT * FROM products";
}

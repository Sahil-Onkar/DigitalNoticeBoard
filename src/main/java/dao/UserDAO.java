package dao;

import model.User;
import util.DBConnection;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Registers a Student (role 3) or Faculty member (role 2).
     * The password is ALWAYS stored as a PBKDF2 hash.
     * Returns false when the email already exists or insertion fails.
     */
    public boolean registerUser(String name, String email, String password,
                                int roleId, int departmentId) {

        if (roleId != 2 && roleId != 3) {
            return false;
        }

        String checkSql = "SELECT user_id FROM users WHERE LOWER(email) = LOWER(?)";
        String insertSql = "INSERT INTO users " +
                "(name, email, password, role_id, department_id, status) " +
                "VALUES (?, ?, ?, ?, ?, 'ACTIVE')";

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement check = connection.prepareStatement(checkSql)) {
                check.setString(1, email);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }

            String hashedPassword = PasswordUtil.hash(password);

            try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
                insert.setString(1, name);
                insert.setString(2, email);
                insert.setString(3, hashedPassword);
                insert.setInt(4, roleId);
                insert.setInt(5, departmentId);
                return insert.executeUpdate() == 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Backward-compatible method for code that used registerStudent().
     */
    public boolean registerStudent(String name, String email, String password,
                                   int departmentId) {
        return registerUser(name, email, password, 3, departmentId);
    }

    /**
     * Upgrades an old plaintext password to PBKDF2 after a successful login.
     */
    public boolean updatePasswordHash(int userId, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, hashedPassword);
            statement.setInt(2, userId);
            return statement.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private User mapUser(ResultSet resultSet) throws Exception {
        User user = new User();

        user.setUserId(resultSet.getInt("user_id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getInt("role_id"));
        user.setDepartmentId(resultSet.getInt("department_id"));

        if (resultSet.getTimestamp("created_at") != null) {
            user.setCreated_at(
                    resultSet.getTimestamp("created_at").toLocalDateTime()
            );
        }

        user.setStatus(resultSet.getString("status"));
        return user;
    }
}

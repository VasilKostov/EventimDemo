package repositories;

import db.DatabaseConnection;
import exceptions.RepositoryException;
import singletons.UsersRoles;

import java.sql.*;

public class UserRepository {

    // SQL constants
    private static final String INSERT_USER_QUERY = "INSERT INTO Users (Name, Password, CreatedAt) VALUES (?, ?, CURRENT_DATE) RETURNING Id";
    private static final String GET_ROLE_ID_QUERY = "SELECT Id FROM Roles WHERE Name = ?";
    private static final String INSERT_USER_ROLE_QUERY = "INSERT INTO UsersToRoles (UserId, RoleId) VALUES (?, ?)";
    private static final String VALIDATE_USER_QUERY = "SELECT id FROM Users WHERE name = ? AND password = ?";
    private static final String GET_USER_ID_QUERY = "SELECT Id FROM Users WHERE Name = ?";
    private static final String GET_USER_ROLE_QUERY = """
            SELECT r.name 
            FROM Users u
            JOIN UsersToRoles ur ON u.id = ur.userId
            JOIN Roles r ON ur.roleId = r.id
            WHERE u.name = ?
            """;
    private static final String IMPORT_ROLE_QUERY = "INSERT INTO Roles (Name) VALUES (?) ON CONFLICT (Name) DO NOTHING";

    // Exception message constants
    private static final String INSERT_USER_EXCEPTION = "Failed to insert user";
    private static final String GET_ROLE_ID_EXCEPTION = "Failed to get role ID";
    private static final String ASSIGN_ROLE_EXCEPTION = "Failed to assign role to user";
    private static final String IMPORT_ROLE_EXCEPTION = "Failed to import role";
    private static final String VALIDATE_USER_EXCEPTION = "Failed to validate user";
    private static final String GET_USER_ID_EXCEPTION = "Failed to get user ID";
    private static final String GET_USER_ROLE_EXCEPTION = "Failed to get user role";

    public int insertUser(String name, String password) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER_QUERY)) {

            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new SQLException("Failed to insert user");

        } catch (SQLException e) {
            throw new RepositoryException(INSERT_USER_EXCEPTION, e);
        }
    }

    public int getRoleId(UsersRoles role) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ROLE_ID_QUERY)) {

            ps.setString(1, role.name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new SQLException("Role not found: " + role.name());

        } catch (SQLException e) {
            throw new RepositoryException(GET_ROLE_ID_EXCEPTION, e);
        }
    }

    public boolean assignRoleToUser(int userId, int roleId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER_ROLE_QUERY)) {

            ps.setInt(1, userId);
            ps.setInt(2, roleId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositoryException(ASSIGN_ROLE_EXCEPTION, e);
        }
    }

    public void importRole(String roleName) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(IMPORT_ROLE_QUERY)) {

            ps.setString(1, roleName);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(IMPORT_ROLE_EXCEPTION + ": " + roleName, e);
        }
    }

    public boolean validateUser(String name, String password) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(VALIDATE_USER_QUERY)) {

            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RepositoryException(VALIDATE_USER_EXCEPTION, e);
        }
    }

    public int getUserId(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_USER_ID_QUERY)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

            return -1;

        } catch (SQLException e) {
            throw new RepositoryException(GET_USER_ID_EXCEPTION, e);
        }
    }

    public UsersRoles getUserRole(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_USER_ROLE_QUERY)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return UsersRoles.valueOf(rs.getString("name"));
            }

            return UsersRoles.Client;

        } catch (SQLException e) {
            throw new RepositoryException(GET_USER_ROLE_EXCEPTION, e);
        }
    }
}

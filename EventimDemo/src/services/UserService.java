package services;

import Singletons.UsersRoles;
import db.DatabaseConnection;
import interfaces.IUserService;

import java.sql.*;

public class UserService implements IUserService {

    @Override
    public boolean createUser(String name, String password) {
        return createUser(name, password, UsersRoles.Client);
    }

    @Override
    public boolean createUser(String name, String password, UsersRoles role) {
        String insertUserSql = "INSERT INTO Users (Name, Password, CreatedAt) VALUES (?, ?, CURRENT_DATE) RETURNING Id";
        String insertUserRoleSql = "INSERT INTO UsersToRoles (UserId, RoleId) VALUES (?, ?)";
        String getRoleIdSql = "SELECT Id FROM Roles WHERE Name = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            int userId;
            try (PreparedStatement ps = connection.prepareStatement(insertUserSql)) {
                ps.setString(1, name);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to insert user");
                    }
                }
            }

            int roleId;
            try (PreparedStatement ps = connection.prepareStatement(getRoleIdSql)) {
                ps.setString(1, role.name());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        roleId = rs.getInt("Id");
                    } else {
                        throw new SQLException("Role not found: " + role.name());
                    }
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(insertUserRoleSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, roleId);
                ps.executeUpdate();
            }

            connection.commit();
            System.out.println("User '" + name + "' created with role '" + role.name() + "' (UserId=" + userId + ")");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void importRoles() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String[] defaultRoles = {"Client", "Admin"};

            for (String roleName : defaultRoles) {
                try (PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Roles (Name) VALUES (?) ON CONFLICT (Name) DO NOTHING")) {
                    ps.setString(1, roleName);
                    ps.executeUpdate();
                }
            }
            System.out.println("Roles imported");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validateUser(String name, String password) {
        String sql = "SELECT id FROM users WHERE name = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getUserIdByUsername(String username) {
        String sql = "SELECT Id FROM Users WHERE Name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

}

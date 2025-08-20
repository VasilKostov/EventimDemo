package repositories;

import exceptions.RepositoryException;
import singletons.UsersRoles;
import db.DatabaseConnection;
import interfaces.IUserRepository;

import java.sql.*;

public class UserRepository implements IUserRepository {

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
            PreparedStatement ps = connection.prepareStatement(insertUserSql);
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userId = rs.getInt(1);
            } else {
                throw new SQLException("Failed to insert user");
            }


            int roleId;
            PreparedStatement ps2 = connection.prepareStatement(getRoleIdSql);
            ps2.setString(1, role.name());
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                roleId = rs2.getInt("Id");
            } else {
                throw new SQLException("Failed show all concerts");
            }

            PreparedStatement ps3 = connection.prepareStatement(insertUserRoleSql);
            ps3.setInt(1, userId);
            ps3.setInt(2, roleId);
            ps3.executeUpdate();

            connection.commit();
            System.out.println("User '" + name + "' created with role '" + role.name() + "' (UserId=" + userId + ")");
            return true;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to create new user", e);
        }
    }

    @Override
    public void importRoles() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String[] defaultRoles = {"Client", "Admin"};

            for (String roleName : defaultRoles) {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO Roles (Name) VALUES (?) ON CONFLICT (Name) DO NOTHING");
                ps.setString(1, roleName);
                ps.executeUpdate();
            }

            System.out.println("Roles imported");
        } catch (SQLException e) {
            throw new RepositoryException("Failed to import default roles", e);
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
            throw new RepositoryException("Failed to validate user", e);
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
            throw new RepositoryException("Failed to get userId by username", e);
        }

        return -1;
    }

    @Override
    public UsersRoles getUserRole(String username) {
        String sql = """
            
                SELECT r.name 
            FROM users u 
            JOIN usersToRoles ur ON u.id = ur.userId 
            JOIN roles r ON ur.roleId = r.id 
            WHERE u.name = ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                String roleName = rs.getString("name");
                return UsersRoles.valueOf(roleName);
            }
        } catch (Exception e) {
            throw new RepositoryException("Failed to get user's role", e);
        }

        return UsersRoles.Client;
    }
}

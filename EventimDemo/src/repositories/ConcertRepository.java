package repositories;

import db.DatabaseConnection;
import exceptions.RepositoryException;
import models.Concerts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConcertRepository {
    private static final String SELECT_ALL_CONCERTS_QUERY = "SELECT * FROM Concerts";
    private static final String SELECT_CONCERT_BY_ID_QUERY = "SELECT * FROM Concerts WHERE Id = ?";
    private static final String INSERT_CONCERT_QUERY = "INSERT INTO Concerts (Name, StartingDate, EndingDate, HallId) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CONCERT_QUERY = "UPDATE Concerts SET Name = ?, StartingDate = ?, EndingDate = ?, HallId = ? WHERE Id = ?";
    private static final String DELETE_CONCERT_QUERY = "DELETE FROM Concerts WHERE Id = ?";

    private static final String SELECT_ALL_CONCERTS_EXCEPTION = "Failed to get all concerts";
    private static final String SELECT_CONCERT_BY_ID_EXCEPTION = "Failed to get concert by id";
    private static final String INSERT_CONCERT_EXCEPTION = "Failed to create concert";
    private static final String UPDATE_CONCERT_EXCEPTION = "Failed to update concert";
    private static final String DELETE_CONCERT_EXCEPTION = "Failed to delete concert";

    public List<Concerts> getAllConcerts() {
        List<Concerts> concerts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_CONCERTS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                concerts.add(
                        new Concerts(rs.getInt("Id"),
                                rs.getString("Name"),
                                rs.getDate("StartingDate"),
                                rs.getDate("EndingDate"),
                                rs.getInt("HallId")));
            }

        } catch (SQLException e) {
            throw new RepositoryException(SELECT_ALL_CONCERTS_EXCEPTION, e);
        }
        return concerts;
    }

    public Concerts getConcertById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_CONCERT_BY_ID_QUERY)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Concerts(rs.getInt("Id"),
                            rs.getString("Name"),
                            rs.getDate("StartingDate"),
                            rs.getDate("EndingDate"),
                            rs.getInt("HallId"));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException(SELECT_CONCERT_BY_ID_EXCEPTION + ": " + id, e);
        }
        return null;
    }

    public boolean insertConcert(Concerts concert) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_CONCERT_QUERY)) {

            ps.setString(1, concert.Name);
            ps.setDate(2, concert.StartingDate);
            ps.setDate(3, concert.EndingDate);
            ps.setInt(4, concert.HallId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositoryException(INSERT_CONCERT_EXCEPTION + ": " + concert.Name, e);
        }
    }

    public boolean updateConcert(Concerts concert) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_CONCERT_QUERY)) {

            ps.setString(1, concert.Name);
            ps.setDate(2, concert.StartingDate);
            ps.setDate(3, concert.EndingDate);
            ps.setInt(4, concert.HallId);
            ps.setInt(5, concert.Id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositoryException(UPDATE_CONCERT_EXCEPTION + ": " + concert.Name, e);
        }
    }

    public boolean deleteConcert(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_CONCERT_QUERY)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositoryException(DELETE_CONCERT_EXCEPTION + ": " + id, e);
        }
    }
}

package repositories;

import exceptions.RepositoryException;
import interfaces.IConcertRepository;
import interfaces.IHallRepository;
import models.Concerts;
import db.DatabaseConnection;
import models.Halls;
import models.dtos.ConcertDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConcertRepository implements IConcertRepository {

    private IHallRepository hallService = new HallRepository();

    @Override
    public List<ConcertDTO> getAllConcerts() {
        List<ConcertDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM concerts";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int hallId = rs.getInt("hallId");
                Halls hall = hallService.getHallById(hallId);

                ConcertDTO dto = new ConcertDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("startingDate"),
                        rs.getDate("endingDate"),
                        hall != null ? hall.Name : "Unknown",
                        hall != null ? hall.Id : -1
                );
                result.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Failed show all concerts", e);
        }

        return result;
    }

    @Override
    public ConcertDTO getConcertById(int id) {
        String sql = "SELECT * FROM concerts WHERE id = ?";
        ConcertDTO concert = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int hallId = rs.getInt("hallId");
                Halls hall = hallService.getHallById(hallId);

                concert = new ConcertDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("startingDate"),
                        rs.getDate("endingDate"),
                        hall != null ? hall.Name : "Unknown",
                        hall != null ? hall.Id : -1
                );
            }
            rs.close();
        } catch (SQLException e) {
            throw new RepositoryException("Failed to get concert by id", e);
        }
        return concert;
    }

    @Override
    public boolean createConcert(Concerts concert) {
        String sql = "INSERT INTO concerts (name, startingDate, endingDate, hallId) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, concert.Name);
            stmt.setDate(2, concert.StartingDate);
            stmt.setDate(3, concert.EndingDate);
            stmt.setInt(4, concert.HallId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to create a concert: " + concert.Name, e);
        }
    }

    @Override
    public boolean updateConcert(Concerts concert) {
        String sql = "UPDATE concerts SET name = ?, startingDate = ?, endingDate = ?, hallId = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, concert.Name);
            stmt.setDate(2, concert.StartingDate);
            stmt.setDate(3, concert.EndingDate);
            stmt.setInt(4, concert.HallId);
            stmt.setInt(5, concert.Id);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to update concert: " + concert.Name, e);
        }
    }

    @Override
    public boolean deleteConcert(int id) {
        String sql = "DELETE FROM concerts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to delete concert", e);
        }
    }
}

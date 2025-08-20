package repositories;

import db.DatabaseConnection;
import exceptions.RepositoryException;
import models.Halls;
import models.dtos.HallDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HallRepository {

    private static final String INSERT_HALL_QUERY = "INSERT INTO Halls(Name) VALUES (?) RETURNING Id";
    private static final String INSERT_SEAT_QUERY = "INSERT INTO Seats (HallId, Row, SeatNumber) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_HALLS_QUERY = "SELECT * FROM Halls";
    private static final String SELECT_HALL_BY_ID_QUERY = "SELECT * FROM Halls WHERE Id = ?";
    private static final String SELECT_AVAILABLE_HALLS_QUERY = """
            SELECT h.Id, h.Name
            FROM Halls h
            WHERE NOT EXISTS (
                     SELECT 1
                     FROM Concerts c
                     WHERE c.HallId = h.Id
                       AND c.StartingDate <= ?
                       AND c.EndingDate >= ?
                     )
            """;
    private static final String INSERT_HALL_EXCEPTION = "Failed to insert hall";
    private static final String INSERT_SEATS_EXCEPTION = "Failed to insert seats";
    private static final String SELECT_ALL_HALLS_EXCEPTION = "Failed to get all halls";
    private static final String SELECT_HALL_BY_ID_EXCEPTION = "Failed to get hall by id";
    private static final String SELECT_AVAILABLE_HALLS_EXCEPTION = "Failed to get available halls";

    public int insertHall(String name) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_HALL_QUERY)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;

        } catch (SQLException e) {
            throw new RepositoryException(INSERT_HALL_EXCEPTION + ": " + name, e);
        }
    }

    public void insertSeats(int hallId, int[] seatsPerRow) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SEAT_QUERY)) {

            for (int row = 0; row < seatsPerRow.length; row++) {
                for (int seatNum = 1; seatNum <= seatsPerRow[row]; seatNum++) {
                    ps.setInt(1, hallId);
                    ps.setInt(2, row + 1);
                    ps.setInt(3, seatNum);
                    ps.addBatch();
                }
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RepositoryException(INSERT_SEATS_EXCEPTION, e);
        }
    }

    public List<Halls> getAllHalls() {
        List<Halls> halls = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_HALLS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Halls hall = new Halls();
                hall.Id = rs.getInt("Id");
                hall.Name = rs.getString("Name");
                halls.add(hall);
            }

        } catch (SQLException e) {
            throw new RepositoryException(SELECT_ALL_HALLS_EXCEPTION, e);
        }
        return halls;
    }

    public Halls getHallById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_HALL_BY_ID_QUERY)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Halls hall = new Halls();
                    hall.Id = rs.getInt("Id");
                    hall.Name = rs.getString("Name");
                    return hall;
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException(SELECT_HALL_BY_ID_EXCEPTION + ": " + id, e);
        }
        return null;
    }

    public List<HallDTO> getAvailableHalls(Date startDate, Date endDate) {
        List<HallDTO> availableHalls = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_AVAILABLE_HALLS_QUERY)) {

            ps.setDate(1, endDate);
            ps.setDate(2, startDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    availableHalls.add(new HallDTO(rs.getInt("Id"), rs.getString("Name")));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException(SELECT_AVAILABLE_HALLS_EXCEPTION, e);
        }
        return availableHalls;
    }
}

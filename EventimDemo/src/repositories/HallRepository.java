package repositories;

import db.DatabaseConnection;
import exceptions.RepositoryException;
import interfaces.IHallRepository;
import models.Halls;
import models.dtos.HallDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HallRepository implements IHallRepository {
    @Override
    public int insertHall(String name) {
        String sql = "INSERT INTO halls(name) VALUES (?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Inserted hall '" + name + "' with ID " + id);
                return id;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Failed to insert hall: " + name, e);
        }
        return -1;
    }

    @Override
    public void insertSeats(int hallId, int[] seatsPerRow) {
        String sql = "INSERT INTO seats (hallid, row, seatnumber) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int row = 0; row < seatsPerRow.length; row++) {
                int seatCount = seatsPerRow[row];
                for (int seatNum = 1; seatNum <= seatCount; seatNum++) {
                    stmt.setInt(1, hallId);
                    stmt.setInt(2, row + 1);
                    stmt.setInt(3, seatNum);
                    stmt.addBatch();
                }
            }

            stmt.executeBatch();
            System.out.println("Inserted all seats for hall ID " + hallId);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to insert seats", e);
        }
    }

    @Override
    public void importHallWithSeats(String name, int[] seatsPerRow) {
        int hallId = insertHall(name);
        if (hallId == -1) {
            System.err.println("Failed to insert hall.");
            return;
        }
        insertSeats(hallId, seatsPerRow);
    }

    @Override
    public List<Halls> getAllHalls() {
        List<Halls> halls = new ArrayList<>();
        String sql = "SELECT * FROM halls";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Halls hall = new Halls();
                hall.Id = rs.getInt("id");
                hall.Name = rs.getString("name");
                halls.add(hall);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Failed show all halls", e);
        }
        return halls;
    }

    @Override
    public Halls getHallById(int id) {
        String sql = "SELECT * FROM halls WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Halls hall = new Halls();
                    hall.Id = rs.getInt("id");
                    hall.Name = rs.getString("name");
                    return hall;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Failed show hall", e);
        }
        return null;
    }

    public List<HallDTO> getAvailableHalls(Date startDate, Date endDate) {
        List<HallDTO> availableHalls = new ArrayList<>();

        String sql = """
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

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, endDate);
            stmt.setDate(2, startDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int hallId = rs.getInt("Id");
                    String name = rs.getString("Name");
                    availableHalls.add(new HallDTO(hallId, name));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Failed to get avalable halls", e);
        }

        return availableHalls;
    }
}

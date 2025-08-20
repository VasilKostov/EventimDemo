package repositories;

import db.DatabaseConnection;
import exceptions.RepositoryException;
import models.dtos.SeatDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private static final String GET_AVAILABLE_SEATS_QUERY = """
                SELECT s.Id, s.Row, s.SeatNumber, s.HallId
                FROM Seats s
                JOIN Concerts c ON s.HallId = c.HallId
                WHERE c.Id = ?
                  AND s.Id NOT IN (
                      SELECT SeatId FROM UsersToSeats
                      WHERE ConcertId = ?
                  )
            """;
    private static final String RESERVE_SEAT_QUERY = """
                INSERT INTO UsersToSeats(UserId, SeatId, ConcertId)
                VALUES (?, ?, ?)
            """;
    private static final String GET_AVAILABLE_SEATS_EXCEPTION = "Failed to get all available seats";
    private static final String RESERVE_SEAT_EXCEPTION = "Failed to reserve a seat";

    public List<SeatDTO> getAvailableSeats(int concertId) {
        List<SeatDTO> seats = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(GET_AVAILABLE_SEATS_QUERY)) {

            stmt.setInt(1, concertId);
            stmt.setInt(2, concertId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seats.add(new SeatDTO(rs.getInt("Id"), rs.getInt("Row"), rs.getInt("SeatNumber"), rs.getInt("HallId")));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException(GET_AVAILABLE_SEATS_EXCEPTION, e);
        }

        return seats;
    }

    public boolean reserveSeat(int userId, int concertId, int seatId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(RESERVE_SEAT_QUERY)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, seatId);
            stmt.setInt(3, concertId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(RESERVE_SEAT_EXCEPTION, e);
        }
    }
}

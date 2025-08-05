package services;

import db.DatabaseConnection;
import interfaces.IOrderService;
import models.dtos.ConcertDTO;
import models.dtos.SeatDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderService implements IOrderService {

    @Override
    public List<SeatDTO> getAvailableSeats(int concertId) {
        List<SeatDTO> seats = new ArrayList<>();
        String sql = """
            SELECT s.Id, s.Row, s.SeatNumber, s.HallId
            FROM Seats s
            JOIN Concerts c ON s.HallId = c.HallId
            WHERE c.Id = ?
              AND s.Id NOT IN (
                  SELECT SeatId FROM UsersToSeats
                  WHERE ConcertId = ?
              )
        """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, concertId);
            stmt.setInt(2, concertId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                seats.add(new SeatDTO(
                        rs.getInt("Id"),
                        rs.getInt("Row"),
                        rs.getInt("SeatNumber"),
                        rs.getInt("HallId")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return seats;
    }

    @Override
    public boolean reserveSeat(int userId, int concertId, int seatId) {
        String sql = """
            INSERT INTO UsersToSeats(UserId, SeatId, ConcertId)
            VALUES (?, ?, ?)
        """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            stmt.setInt(2, seatId);
            stmt.setInt(3, concertId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

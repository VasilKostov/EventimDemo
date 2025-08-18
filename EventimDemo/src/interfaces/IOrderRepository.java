package interfaces;

import models.dtos.SeatDTO;

import java.util.List;

public interface IOrderRepository {
    List<SeatDTO> getAvailableSeats(int concertId);
    boolean reserveSeat(int userId, int concertId, int seatId);
}

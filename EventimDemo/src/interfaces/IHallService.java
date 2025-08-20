package interfaces;

import models.Halls;
import models.dtos.HallDTO;

import java.sql.Date;
import java.util.List;

public interface IHallService {
    int insertHall(String name);
    void insertSeats(int hallId, int[] seatsPerRow);
    void importHallWithSeats(String name, int[] seatsPerRow);
    List<Halls> getAllHalls();
    Halls getHallById(int id);
    List<HallDTO> getAvailableHalls(Date startDate, Date endDate);
}

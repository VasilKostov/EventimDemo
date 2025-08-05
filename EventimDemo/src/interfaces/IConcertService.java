package interfaces;

import java.util.List;
import models.Concerts;
import models.dtos.ConcertDTO;

public interface IConcertService {
    List<ConcertDTO> getAllConcerts();
    ConcertDTO getConcertById(int id);

    // Required admin role
    boolean createConcert(Concerts concert);
    boolean updateConcert(Concerts concert);
    boolean deleteConcert(int id);
}

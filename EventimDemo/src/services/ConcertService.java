package services;

import controllers.HallController;
import interfaces.IConcertService;
import interfaces.IHallService;
import models.Concerts;
import models.Halls;
import models.dtos.ConcertDTO;
import repositories.ConcertRepository;

import java.util.ArrayList;
import java.util.List;

public class ConcertService implements IConcertService {

    private final ConcertRepository concertRepository;
    private final HallController hallController;

    public ConcertService(ConcertRepository concertRepository, HallController hallController) {
        this.concertRepository = concertRepository;
        this.hallController = hallController;
    }

    @Override
    public List<ConcertDTO> getAllConcerts() {
        List<ConcertDTO> result = new ArrayList<>();

        for (Concerts concert : concertRepository.getAllConcerts()) {
            Halls hall = hallController.getHall(concert.HallId);
            result.add(new ConcertDTO(
                    concert.Id,
                    concert.Name,
                    concert.StartingDate,
                    concert.EndingDate,
                    hall != null ? hall.Name : "Unknown",
                    hall != null ? hall.Id : -1
            ));
        }
        return result;
    }

    @Override
    public ConcertDTO getConcertById(int id) {
        Concerts concert = concertRepository.getConcertById(id);
        if (concert == null) {
            return null;
        }

        Halls hall = hallController.getHall(concert.HallId);
        return new ConcertDTO(
                concert.Id,
                concert.Name,
                concert.StartingDate,
                concert.EndingDate,
                hall != null ? hall.Name : "Unknown",
                hall != null ? hall.Id : -1
        );
    }

    @Override
    public boolean createConcert(Concerts concert) {
        if(concert == null) {
            return false;
        }

        return concertRepository.insertConcert(concert);
    }

    @Override
    public boolean updateConcert(Concerts concert) {
        if(concert == null) {
            return false;
        }

        return concertRepository.updateConcert(concert);
    }

    @Override
    public boolean deleteConcert(int id) {
        if(id < 0) {
            return false;
        }

        return concertRepository.deleteConcert(id);
    }
}

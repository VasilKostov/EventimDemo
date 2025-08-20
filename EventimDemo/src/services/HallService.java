package services;

import interfaces.IHallService;
import models.Halls;
import models.dtos.HallDTO;
import repositories.HallRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HallService implements IHallService {

    private final HallRepository hallRepository;

    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @Override
    public int insertHall(String name) {
        if (name.isBlank()) {
            return -1;
        }

        int id = hallRepository.insertHall(name);
        if (id >= 0) {
            System.out.println("Inserted hall '" + name + "' with ID " + id);
        }
        return id;
    }

    @Override
    public void insertSeats(int hallId, int[] seatsPerRow) {
        hallRepository.insertSeats(hallId, seatsPerRow);
    }

    @Override
    public void importHallWithSeats(String name, int[] seatsPerRow) {
        if(name.isBlank()) {
            return;
        }

        int hallId = insertHall(name);
        if (hallId >= 0) {
            insertSeats(hallId, seatsPerRow);
        }
    }

    @Override
    public List<Halls> getAllHalls() {
        return hallRepository.getAllHalls();
    }

    @Override
    public Halls getHallById(int id) {
        if(id < 0) {
            return null;
        }

        return hallRepository.getHallById(id);
    }

    @Override
    public List<HallDTO> getAvailableHalls(Date startDate, Date endDate) {
        if(startDate.compareTo(endDate) > 0) {
            return new ArrayList<>();
        }

        return hallRepository.getAvailableHalls(startDate, endDate);
    }
}

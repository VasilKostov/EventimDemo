package controllers;

import interfaces.IConcertService;
import models.Concerts;
import models.dtos.ConcertDTO;

import java.util.List;

public class ConcertController {

    private final IConcertService concertService;

    public ConcertController(IConcertService concertService) {
        this.concertService = concertService;
    }

    public void showAllConcerts() {
        List<ConcertDTO> concerts = concertService.getAllConcerts();
        if (concerts.isEmpty()) {
            System.out.println("No concerts found.");
        } else {
            concerts.forEach(c -> System.out.println(c.toString()));
        }
    }

    public ConcertDTO getConcertById(int id) {
        ConcertDTO concert = concertService.getConcertById(id);
        if (concert == null) {
            System.out.println("Concert not found: " + id);
        } else {
            System.out.println(concert.toString());
        }

        return concert;
    }

    public void addConcert(Concerts concert) {
        if (concertService.createConcert(concert)) {
            System.out.println("Concert added successfully: " + concert.Name);
        } else {
            System.out.println("Failed to add concert: " + concert.Name);
        }
    }

    public void editConcert(Concerts concert) {
        if (concertService.updateConcert(concert)) {
            System.out.println("Concert updated: " + concert.Name);
        } else {
            System.out.println("Failed to update concert: " + concert.Name);
        }
    }

    public void removeConcert(int id) {
        if (concertService.deleteConcert(id)) {
            System.out.println("Concert deleted: " + id);
        } else {
            System.out.println("Failed to delete concert: " + id);
        }
    }
}

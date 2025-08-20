package controllers;

import interfaces.IConcertService;
import interfaces.IHallService;
import models.Halls;
import models.dtos.HallDTO;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class HallController {

    private final IHallService hallService;
    private final Scanner scanner;

    public HallController(IHallService hallService, Scanner scanner) {
        this.hallService = hallService;
        this.scanner = scanner;
    }

    public void showAllHalls() {
        List<Halls> halls = hallService.getAllHalls();

        if (halls.isEmpty()) {
            System.out.println("No halls found.");
        } else {
            halls.forEach(h -> System.out.println(h.toString()));
        }
    }

    public void addHallWithSeats(String name, int[] seatsPerRow) {
        hallService.importHallWithSeats(name, seatsPerRow);

        System.out.println("Hall added successfully: " + name);
    }

    public Halls getHall(int id){
        Halls hall = hallService.getHallById(id);

        if (hall == null) {
            System.out.println("Hall with id " + id + " not found.");

            return null;
        }

        return hall;
    }

    public int selectHallByNameAndDateRange(Date startDate, Date endDate) {
        List<HallDTO> halls = hallService.getAvailableHalls(startDate, endDate);

        if (halls.isEmpty()) {
            System.out.println("No halls available in the selected date range.");

            return -1;
        }

        while (true) {
            System.out.print("Search hall by name (or type 'list' to see all, 'cancel' to exit): ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                return -1;
            }

            List<HallDTO> filteredHalls;

            if (input.equalsIgnoreCase("list") || input.isBlank()) {
                filteredHalls = halls;
            } else {
                filteredHalls = halls.stream()
                        .filter(h -> h.getName().toLowerCase().contains(input.toLowerCase()))
                        .toList();
            }

            if (filteredHalls.isEmpty()) {
                System.out.println("No halls found matching '" + input + "'");

                continue;
            }

            System.out.println("Available halls:");

            for (HallDTO hall : filteredHalls) {
                System.out.println(hall.toString());
            }

            System.out.print("Enter hall ID to select (or 'cancel' to exit): ");

            String hallIdInput = scanner.nextLine().trim();

            if (hallIdInput.equalsIgnoreCase("cancel")) {
                return -1;
            }

            try {
                int hallId = Integer.parseInt(hallIdInput);
                boolean exists = filteredHalls.stream().anyMatch(h -> h.getId() == hallId);
                if (exists) {
                    return hallId;
                }
                else {
                    System.out.println("Invalid hall ID selected.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric hall ID.");
            }
        }
    }
}

package singletons;

import interfaces.ICommand;
import interfaces.IConcertRepository;
import interfaces.IHallRepository;
import interfaces.IMenu;
import models.dtos.HallDTO;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Helper {
    public static  int selectHallByNameAndDateRange(Date startDate, Date endDate, Scanner scanner, IConcertRepository concertRepository, IHallRepository hallRepository) {
        List<HallDTO> halls = hallRepository.getAvailableHalls(startDate, endDate);

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
                System.out.printf("ID: %d, Name: %s%n", hall.getId(), hall.getName());
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

    public static void executeCommand(Map<Integer, ICommand> commands, int choice){
        ICommand command = commands.get(choice);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid choice");
        }
    }

    public static void executeCommandWithMenu(Map<Integer, ICommand> commands, int choice){
        ICommand command = commands.get(choice);
        if (command != null) {
            IMenu menu = command.execute();
            if(menu != null){
                menu.start();
            }
        } else {
            System.out.println("Invalid choice");
        }
    }
}

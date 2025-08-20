package commands.concert;

import controllers.ConcertController;
import controllers.HallController;
import interfaces.ICommand;
import interfaces.IMenu;
import models.Concerts;
import models.dtos.ConcertDTO;
import singletons.Helper;

import java.sql.Date;
import java.util.Scanner;

public class UpdateConcertCommand implements ICommand {
    private final Scanner scanner;
    private final ConcertController concertController;
    private final HallController hallController;

    public UpdateConcertCommand(Scanner scanner, ConcertController concertController, HallController hallController) {
        this.scanner = scanner;
        this.concertController = concertController;
        this.hallController = hallController;
    }

    @Override
    public IMenu execute() {
        System.out.print("Enter Concert ID to update: ");

        int id = Integer.parseInt(scanner.nextLine());
        ConcertDTO existing = concertController.getConcertById(id);
        if (existing == null) {
            System.out.println("Concert not found.");

            return null;
        }

        System.out.print("Name (" + existing.getName() + "): ");
        String name = scanner.nextLine();

        if (!name.isBlank()) {
            existing.setName(name);
        }

        System.out.print("Starting date (" + existing.getStartingDate() + ") yyyy-mm-dd: ");

        String startDateStr = scanner.nextLine();
        Date startDate = existing.getStartingDate();

        if (!startDateStr.isBlank()){
            startDate = Date.valueOf(startDateStr);
        }

        System.out.print("Ending date (" + existing.getEndingDate() + ") yyyy-mm-dd: ");
        String endDateStr = scanner.nextLine();
        Date endDate = existing.getEndingDate();

        if (!endDateStr.isBlank()){
            endDate = Date.valueOf(endDateStr);
        }

        System.out.println("Current hall: " + existing.getHallName());
        System.out.print("Change hall? (yes/no): ");
        String changeHall = scanner.nextLine();

        if (changeHall.equalsIgnoreCase("yes")) {
            int hallId = hallController.selectHallByNameAndDateRange(startDate, endDate);
            if (hallId != -1) {
                existing.setHallId(hallId);
            } else {
                System.out.println("Hall not changed.");
            }
        }

        existing.setStartingDate(startDate);
        existing.setEndingDate(endDate);

        concertController.editConcert(new Concerts(existing.getId(), existing.getName(), existing.getStartingDate(), existing.getEndingDate(), existing.getHallId()));

        return null;
    }
}

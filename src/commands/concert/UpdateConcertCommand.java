package commands.concert;

import interfaces.ICommand;
import interfaces.IConcertRepository;
import interfaces.IHallRepository;
import interfaces.IMenu;
import models.Concerts;
import models.dtos.ConcertDTO;
import models.dtos.HallDTO;
import singletons.Helper;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class UpdateConcertCommand implements ICommand {
    private final Scanner scanner;
    private final IConcertRepository concertRepository;
    private final IHallRepository hallRepository;

    public UpdateConcertCommand(Scanner scanner, IConcertRepository concertRepository, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.concertRepository = concertRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public IMenu execute() {
        System.out.print("Enter Concert ID to update: ");

        int id = Integer.parseInt(scanner.nextLine());
        ConcertDTO existing = concertRepository.getConcertById(id);
        if (existing == null) {
            System.out.println("Concert not found.");

            return null;
        }

        System.out.print("Name (" + existing.Name + "): ");

        String name = scanner.nextLine();
        if (!name.isBlank()) {
            existing.Name = name;
        }

        System.out.print("Starting date (" + existing.StartingDate + ") yyyy-mm-dd: ");

        String startDateStr = scanner.nextLine();
        Date startDate = existing.StartingDate;

        if (!startDateStr.isBlank()){
            startDate = Date.valueOf(startDateStr);
        }

        System.out.print("Ending date (" + existing.EndingDate + ") yyyy-mm-dd: ");
        String endDateStr = scanner.nextLine();
        Date endDate = existing.EndingDate;

        if (!endDateStr.isBlank()){
            endDate = Date.valueOf(endDateStr);
        }

        System.out.println("Current hall: " + existing.HallName);
        System.out.print("Change hall? (yes/no): ");
        String changeHall = scanner.nextLine();

        if (changeHall.equalsIgnoreCase("yes")) {
            int hallId = Helper.selectHallByNameAndDateRange(startDate, endDate, scanner, concertRepository, hallRepository);
            if (hallId != -1) {
                existing.HallId = hallId;
            } else {
                System.out.println("Hall not changed.");
            }
        }

        existing.StartingDate = startDate;
        existing.EndingDate = endDate;

        boolean updated = concertRepository.updateConcert(new Concerts(existing.Id, existing.Name, existing.StartingDate, existing.EndingDate, existing.HallId));
        System.out.println(updated ? "Concert updated." : "Failed to update concert.");

        return null;
    }
}

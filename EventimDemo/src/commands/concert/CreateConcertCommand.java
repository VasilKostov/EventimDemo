package commands.concert;

import interfaces.ICommand;
import interfaces.IConcertRepository;
import interfaces.IHallRepository;
import interfaces.IMenu;
import models.Concerts;
import models.dtos.HallDTO;
import singletons.Helper;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class CreateConcertCommand implements ICommand {
    private final Scanner scanner;
    private final IConcertRepository concertRepository;
    private final IHallRepository hallRepository;

    public CreateConcertCommand(Scanner scanner, IConcertRepository concertRepository, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.concertRepository = concertRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public IMenu execute() {
        System.out.println("=== Create Concert ===");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Starting date (yyyy-mm-dd): ");
        Date startDate = Date.valueOf(scanner.nextLine());

        System.out.print("Ending date (yyyy-mm-dd): ");
        Date endDate = Date.valueOf(scanner.nextLine());
        int hallId = Helper.selectHallByNameAndDateRange(startDate, endDate, scanner,  concertRepository, hallRepository);
        if (hallId == -1) {
            System.out.println("No hall selected. Concert creation cancelled.");
            return null;
        }

        Concerts concert = new Concerts(name, startDate, endDate, hallId);

        System.out.println(concertRepository.createConcert(concert) ? "Concert created." : "Failed to create concert.");

        return null;
    }
}

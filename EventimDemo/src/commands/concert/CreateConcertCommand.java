package commands.concert;

import controllers.ConcertController;
import controllers.HallController;
import interfaces.ICommand;
import interfaces.IConcertService;
import interfaces.IHallService;
import interfaces.IMenu;
import models.Concerts;
import org.postgresql.replication.fluent.logical.ChainedLogicalCreateSlotBuilder;
import singletons.Helper;

import java.sql.Date;
import java.util.Scanner;

public class CreateConcertCommand implements ICommand {
    private final Scanner scanner;
    private final ConcertController concertController;
    private final HallController hallController;

    public CreateConcertCommand(Scanner scanner, ConcertController concertController, HallController hallController) {
        this.scanner = scanner;
        this.concertController = concertController;
        this.hallController = hallController;
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

        int hallId = hallController.selectHallByNameAndDateRange(startDate, endDate);
        if (hallId == -1) {
            System.out.println("No hall selected. Concert creation cancelled.");
            return null;
        }

        concertController.addConcert(new Concerts(name, startDate, endDate, hallId));

        return null;
    }
}

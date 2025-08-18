package commands.concert;

import interfaces.ICommand;
import interfaces.IConcertRepository;
import interfaces.IMenu;

import java.util.Scanner;

public class DeleteConcertCommand implements ICommand {
    private final Scanner scanner;
    private final IConcertRepository concertRepository;

    public DeleteConcertCommand(Scanner scanner, IConcertRepository concertRepository) {
        this.scanner = scanner;
        this.concertRepository = concertRepository;
    }

    @Override
    public IMenu execute() {
        System.out.print("Enter Concert ID to delete: ");

        int id = Integer.parseInt(scanner.nextLine());
        boolean deleted = concertRepository.deleteConcert(id);

        System.out.println(deleted ? "Concert deleted." : "Failed to delete concert.");

        return null;
    }
}

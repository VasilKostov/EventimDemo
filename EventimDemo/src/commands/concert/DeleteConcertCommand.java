package commands.concert;

import controllers.ConcertController;
import interfaces.ICommand;
import interfaces.IConcertService;
import interfaces.IMenu;

import java.util.Scanner;

public class DeleteConcertCommand implements ICommand {
    private final Scanner scanner;
    private final ConcertController concertController;

    public DeleteConcertCommand(Scanner scanner, ConcertController concertController) {
        this.scanner = scanner;
        this.concertController = concertController;
    }

    @Override
    public IMenu execute() {
        System.out.print("Enter Concert ID to delete: ");

        int id = Integer.parseInt(scanner.nextLine());

        concertController.removeConcert(id);

        return null;
    }
}

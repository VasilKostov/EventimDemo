package commands.hall;

import interfaces.ICommand;
import interfaces.IHallRepository;
import interfaces.IMenu;
import repositories.HallRepository;

import java.util.Scanner;

public class CreateHallCommand implements ICommand {
    private final Scanner scanner;
    private final IHallRepository hallRepository;

    public CreateHallCommand(Scanner scanner, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.hallRepository = hallRepository;
    }

    @Override
    public IMenu execute() {
        System.out.print("Enter Hall name: ");

        String name = scanner.nextLine();

        System.out.print("Enter number of rows: ");

        int rows = Integer.parseInt(scanner.nextLine());
        int[] seatsPerRow = new int[rows];

        for (int i = 0; i < rows; i++) {
            System.out.print("Enter number of seats in row " + (i + 1) + ": ");

            seatsPerRow[i] = Integer.parseInt(scanner.nextLine());
        }

        hallRepository.importHallWithSeats(name, seatsPerRow);
        System.out.println("Hall created.");

        return null;
    }
}

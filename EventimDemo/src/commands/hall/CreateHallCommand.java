package commands.hall;

import controllers.HallController;
import interfaces.ICommand;
import interfaces.IHallService;
import interfaces.IMenu;
import menus.HallMenu;

import java.util.Scanner;

public class CreateHallCommand implements ICommand {
    private final Scanner scanner;
    private final HallController hallController;

    public CreateHallCommand(Scanner scanner, HallController hallController) {
        this.scanner = scanner;
        this.hallController = hallController;
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

        hallController.addHallWithSeats(name, seatsPerRow);

        System.out.println("Hall created.");

        return null;
    }
}

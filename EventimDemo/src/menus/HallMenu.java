package menus;

import commands.hall.CreateHallCommand;
import commands.hall.ListHallsCommand;
import interfaces.*;
import singletons.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HallMenu implements IMenu {
    private final Map<Integer, ICommand> commands = new HashMap<>();
    private final Scanner scanner;
    private final IHallRepository hallRepository;

    public HallMenu(Scanner scanner, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.hallRepository = hallRepository;

        commands.put(1, new ListHallsCommand(hallRepository));
        commands.put(2, new CreateHallCommand(scanner, hallRepository));
    }
    @Override
    public void start() {
        while (true) {
            System.out.println("\n=== Manage Halls ===");
            System.out.println("1. List halls");
            System.out.println("2. Create hall");
            System.out.println("3. Back");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            if(choice == 3){
                break;
            }

            Helper.executeCommandWithMenu(commands, choice);
        }
    }
}

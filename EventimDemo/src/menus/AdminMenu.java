package menus;

import commands.concert.ManageConcertsCommand;
import commands.concert.ViewConcertsCommand;
import commands.hall.ManageHallCommand;
import controllers.ConcertController;
import controllers.HallController;
import controllers.OrderController;
import controllers.UserController;
import interfaces.*;
import singletons.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminMenu implements IMenu {
    private final Map<Integer, ICommand> commands = new HashMap<>();
    private final Scanner scanner;

    public AdminMenu(Scanner scanner, ConcertController concertController, OrderController orderController, UserController userController, HallController hallController) {
        this.scanner = scanner;

        commands.put(1, new ViewConcertsCommand(concertController));
        commands.put(2, new ManageConcertsCommand(scanner, concertController, orderController, userController, hallController));
        commands.put(3, new ManageHallCommand(scanner, hallController));
    }


    @Override
    public void start() {
        while (true) {
            System.out.println("\n=== Mian Menu ===");
            System.out.println("1. View Concerts");
            System.out.println("2. Manage Concerts");
            System.out.println("3. Manage Halls");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            if(choice == 4)
            {
                break;
            }

            Helper.executeCommandWithMenu(commands, choice);
        }
    }
}

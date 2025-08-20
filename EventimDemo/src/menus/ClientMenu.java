package menus;

import commands.concert.ReservationCommand;
import commands.concert.ViewConcertsCommand;
import controllers.ConcertController;
import controllers.OrderController;
import controllers.UserController;
import interfaces.*;
import singletons.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientMenu implements IMenu {
    private final Map<Integer, ICommand> commands = new HashMap<>();
    private final Scanner scanner;

    public ClientMenu(Scanner scanner, ConcertController concertController, OrderController orderController, UserController userController) {
        this.scanner = scanner;

        commands.put(1, new ViewConcertsCommand(concertController));
        commands.put(2, new ReservationCommand(scanner, concertController,orderController, userController));
    }

    @Override
    public void start(){
        while (true) {
            System.out.println("\n=== Mian Menu ===");
            System.out.println("1. View Concerts");
            System.out.println("2. Order Seat");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            if(choice == 3)
            {
                break;
            }

            Helper.executeCommand(commands, choice);
        }
    }
}

package menus;

import commands.LoginCommand;
import commands.RegisterCommand;
import controllers.ConcertController;
import controllers.HallController;
import controllers.OrderController;
import controllers.UserController;
import interfaces.*;
import singletons.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginMenu implements IMenu {
    private final Map<Integer, ICommand> commands = new HashMap<>();
    private final UserController userController;
    private final OrderController orderController;
    private final ConcertController concertController;
    private final Scanner scanner;
    private final HallController hallController;

    public LoginMenu(Scanner scanner, ConcertController concertController, OrderController orderController, UserController userController, HallController hallController) {

        this.scanner = scanner;
        this.userController = userController;
        this.orderController = orderController;
        this.concertController = concertController;
        this.hallController = hallController;

        commands.put(1, new RegisterCommand(scanner, concertController, orderController, userController));
        commands.put(2, new LoginCommand(scanner, concertController, orderController, userController, hallController));
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Welcome ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 3)
            {
                System.out.println("Good Bye!");
                break;
            }

            Helper.executeCommandWithMenu(commands, choice);
        }
    }
}

package menus;

import commands.concert.ManageConcertsCommand;
import commands.concert.ViewConcertsCommand;
import commands.hall.ManageHallCommand;
import interfaces.*;
import singletons.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminMenu implements IMenu {
    private final Map<Integer, ICommand> commands = new HashMap<>();
    private final Scanner scanner;

    public AdminMenu(Scanner scanner, IConcertRepository concertRepository, IOrderRepository orderRepository, IUserRepository userRepository, IHallRepository hallRepository) {
        this.scanner = scanner;

        commands.put(1, new ViewConcertsCommand(concertRepository));
        commands.put(2, new ManageConcertsCommand(scanner, concertRepository, orderRepository, userRepository, hallRepository));
        commands.put(3, new ManageHallCommand(scanner, hallRepository));
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

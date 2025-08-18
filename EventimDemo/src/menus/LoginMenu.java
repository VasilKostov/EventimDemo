package menus;

import commands.LoginCommand;
import commands.RegisterCommand;
import interfaces.*;
import singletons.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginMenu implements IMenu {
    private final Map<Integer, ICommand> commands = new HashMap<>();
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;
    private final IConcertRepository concertRepository;
    private final Scanner scanner;
    private final IHallRepository hallRepository;

    public LoginMenu(Scanner scanner, IConcertRepository concertRepository, IOrderRepository orderRepository, IUserRepository userRepository, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.concertRepository = concertRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.hallRepository = hallRepository;

        commands.put(1, new RegisterCommand(scanner, concertRepository, orderRepository, userRepository, hallRepository));
        commands.put(2, new LoginCommand(scanner, concertRepository, orderRepository, userRepository, hallRepository));
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

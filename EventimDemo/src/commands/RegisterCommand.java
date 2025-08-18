package commands;

import interfaces.*;
import menus.ClientMenu;

import java.util.Scanner;

public class RegisterCommand implements ICommand {
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;
    private final IConcertRepository concertRepository;
    private final Scanner scanner;
    private final IHallRepository hallRepository;

    public RegisterCommand(Scanner scanner, IConcertRepository concertRepository, IOrderRepository orderRepository, IUserRepository userRepository, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.concertRepository = concertRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public IMenu execute() {
        System.out.println("--- Register ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            boolean res = userRepository.createUser(username, password);
            if (res) {
                System.out.println("User registered as a client successfully!");

                return new ClientMenu(scanner, concertRepository, orderRepository, userRepository);
            } else {
                System.out.println("User registration failed.");
            }
        } catch (Exception e) {
            System.out.println("Failed to register user: " + e.getMessage());
        }

        return null;
    }
}

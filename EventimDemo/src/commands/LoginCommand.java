package commands;

import interfaces.*;
import menus.AdminMenu;
import menus.ClientMenu;
import singletons.UsersRoles;

import java.util.Scanner;

public class LoginCommand implements ICommand {
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;
    private final IConcertRepository concertRepository;
    private final Scanner scanner;
    private final IHallRepository hallRepository;

    public LoginCommand(Scanner scanner, IConcertRepository concertRepository, IOrderRepository orderRepository, IUserRepository userRepository, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.concertRepository = concertRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public IMenu execute(){
        System.out.println("==== Login ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userRepository.validateUser(username, password)) {
            UsersRoles role = userRepository.getUserRole(username);

            if(role == UsersRoles.Client){
                System.out.println("Login successful! Welcome, " + username);

                return new ClientMenu(scanner, concertRepository, orderRepository, userRepository);
            }
            else if(role == UsersRoles.Admin){
                System.out.println("Login successful! Welcome, " + username);

                return new AdminMenu(scanner, concertRepository, orderRepository, userRepository, hallRepository);
            }
            else{
                System.out.println("Internal error");

                return null;
            }
        } else {
            System.out.println("Invalid username or password.");
        }

        return null;
    }
}

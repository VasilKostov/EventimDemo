package commands;

import controllers.ConcertController;
import controllers.HallController;
import controllers.OrderController;
import controllers.UserController;
import interfaces.*;
import menus.AdminMenu;
import menus.ClientMenu;
import singletons.UsersRoles;

import java.util.Scanner;

public class LoginCommand implements ICommand {
    private final UserController userController;
    private final OrderController orderController;
    private final ConcertController concertController;
    private final Scanner scanner;
    private final HallController hallController;

    public LoginCommand(Scanner scanner, ConcertController concertController, OrderController orderController, UserController userController, HallController hallController) {
        this.scanner = scanner;
        this.userController = userController;
        this.orderController = orderController;
        this.concertController = concertController;
        this.hallController = hallController;
    }

    @Override
    public IMenu execute(){
        System.out.println("==== Login ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userController.validateUser(username, password)) {
            UsersRoles role = userController.getUserRole(username);

            if(role == UsersRoles.Client){
                System.out.println("Login successful! Welcome, " + username);

                return new ClientMenu(scanner, concertController, orderController, userController);
            }
            else if(role == UsersRoles.Admin){
                System.out.println("Login successful! Welcome, " + username);

                return new AdminMenu(scanner, concertController, orderController, userController, hallController);
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

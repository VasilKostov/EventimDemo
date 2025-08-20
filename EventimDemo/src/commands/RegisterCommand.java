package commands;

import controllers.*;
import interfaces.*;
import menus.ClientMenu;

import java.util.Scanner;

public class RegisterCommand implements ICommand {
    private final UserController userController;
    private final OrderController orderController;
    private final ConcertController concertController;
    private final Scanner scanner;

    public RegisterCommand(Scanner scanner, ConcertController concertController, OrderController orderController, UserController userController) {
        this.scanner = scanner;
        this.userController = userController;
        this.orderController = orderController;
        this.concertController = concertController;
    }

    @Override
    public IMenu execute() {
        System.out.println("--- Register ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            boolean success = userController.createUser(username, password);
            if(success){
                return new ClientMenu(scanner, concertController, orderController, userController);
            }
        } catch (Exception e) {
            System.out.println("Failed to register user: " + e.getMessage());
        }

        return null;
    }
}

package commands.concert;

import controllers.ConcertController;
import controllers.HallController;
import controllers.OrderController;
import controllers.UserController;
import interfaces.*;
import menus.ConcertMenu;

import java.util.Scanner;

public class ManageConcertsCommand implements ICommand {
    private final UserController userController;
    private final OrderController orderController;
    private final ConcertController concertController;
    private final Scanner scanner;
    private final HallController hallController;

    public ManageConcertsCommand(Scanner scanner, ConcertController concertController, OrderController orderController, UserController userController, HallController hallController) {
        this.scanner = scanner;
        this.concertController = concertController;
        this.orderController =  orderController;
        this.userController = userController;
        this.hallController = hallController;
    }

    @Override
    public IMenu execute() {
        return new ConcertMenu(scanner, concertController, orderController, userController, hallController);
    }
}

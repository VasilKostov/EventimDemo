package commands.concert;

import controllers.ConcertController;
import controllers.OrderController;
import controllers.UserController;
import interfaces.*;

import java.util.Scanner;

public class ReservationCommand implements ICommand {
    private final UserController userController;
    private final OrderController orderController;
    private final ConcertController concertController;
    private final Scanner scanner;

    public ReservationCommand(Scanner scanner, ConcertController concertController, OrderController orderController, UserController userController) {
        this.scanner = scanner;
        this.concertController = concertController;
        this.orderController = orderController;
        this.userController = userController;
    }

    @Override
    public IMenu execute() {
        System.out.println("=== Concert Reservation ===");

        concertController.showAllConcerts();

        System.out.print("Enter Concert ID to3 reserve: ");
        int concertId = Integer.parseInt(scanner.nextLine());

        orderController.showAvailableSeats(concertId);

        System.out.print("Enter Seat ID to reserve: ");
        int seatId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter your Username for recipient: ");

        String loggedUsername= scanner.nextLine();
        int userId = userController.getUserId(loggedUsername);

        orderController.reserveSeat(userId, concertId, seatId);

        return null;
    }
}

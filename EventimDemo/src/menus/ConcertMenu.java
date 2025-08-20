package menus;

import commands.concert.CreateConcertCommand;
import commands.concert.DeleteConcertCommand;
import commands.concert.ListConcertsCommand;
import commands.concert.UpdateConcertCommand;
import controllers.ConcertController;
import controllers.HallController;
import controllers.OrderController;
import controllers.UserController;
import interfaces.*;
import models.Concerts;
import org.postgresql.replication.fluent.logical.ChainedLogicalCreateSlotBuilder;
import singletons.Helper;

import java.util.FormatterClosedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConcertMenu implements IMenu {
    private final Map<Integer, ICommand> commands = new HashMap<>();
    private final UserController userController;
    private final OrderController orderController;
    private final ConcertController concertController;
    private final Scanner scanner;
    private final HallController hallController;

    public ConcertMenu(Scanner scanner, ConcertController concertController, OrderController orderController, UserController userController, HallController hallController) {
        this.scanner = scanner;
        this.concertController = concertController;
        this.orderController = orderController;
        this.userController = userController;
        this.hallController = hallController;

        commands.put(1, new ListConcertsCommand(concertController));
        commands.put(2, new CreateConcertCommand(scanner, concertController, hallController));
        commands.put(3, new UpdateConcertCommand(scanner, concertController, hallController));
        commands.put(4, new DeleteConcertCommand(scanner, concertController));
    }
    @Override
    public void start() {
        while (true) {
            System.out.println("\n==== Manage Concerts ===");
            System.out.println("1. List concerts");
            System.out.println("2. Create concert");
            System.out.println("3. Update concert");
            System.out.println("4. Delete concert");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            if(choice == 5){
                break;
            }

            Helper.executeCommand(commands, choice);
        }
    }
}

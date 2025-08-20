package menus;

import commands.concert.CreateConcertCommand;
import commands.concert.DeleteConcertCommand;
import commands.concert.ListConcertsCommand;
import commands.concert.UpdateConcertCommand;
import interfaces.*;
import singletons.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConcertMenu implements IMenu {
    private final Map<Integer, ICommand> commands = new HashMap<>();
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;
    private final IConcertRepository concertRepository;
    private final Scanner scanner;
    private final IHallRepository hallRepository;

    public ConcertMenu(Scanner scanner, IConcertRepository concertRepository, IOrderRepository orderRepository, IUserRepository userRepository, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.concertRepository = concertRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.hallRepository = hallRepository;

        commands.put(1, new ListConcertsCommand(concertRepository));
        commands.put(2, new CreateConcertCommand(scanner, concertRepository, hallRepository));
        commands.put(3, new UpdateConcertCommand(scanner, concertRepository, hallRepository));
        commands.put(4, new DeleteConcertCommand(scanner, concertRepository));
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

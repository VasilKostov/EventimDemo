package commands.concert;

import interfaces.*;
import menus.ConcertMenu;

import java.util.Scanner;

public class ManageConcertsCommand implements ICommand {
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;
    private final IConcertRepository concertRepository;
    private final Scanner scanner;
    private final IHallRepository hallRepository;

    public ManageConcertsCommand(Scanner scanner, IConcertRepository concertRepository, IOrderRepository orderRepository, IUserRepository userRepository, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.concertRepository = concertRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public IMenu execute() {
        return new ConcertMenu(scanner, concertRepository, orderRepository, userRepository, hallRepository);
    }
}

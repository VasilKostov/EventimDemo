package commands;

import interfaces.*;
import models.dtos.ConcertDTO;
import models.dtos.SeatDTO;

import java.util.List;
import java.util.Scanner;

public class ReservationCommand implements ICommand {
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;
    private final IConcertRepository concertRepository;
    private final Scanner scanner;

    public ReservationCommand(Scanner scanner, IConcertRepository concertRepository, IOrderRepository orderRepository, IUserRepository userRepository) {
        this.scanner = scanner;
        this.concertRepository = concertRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public IMenu execute() {
        System.out.println("=== Concert Reservation ===");

        List<ConcertDTO> concerts = concertRepository.getAllConcerts();
        if (concerts.isEmpty()) {
            System.out.println("No concerts available.");
            return null;
        }

        System.out.println("Available Concerts:");
        for (ConcertDTO concert : concerts) {
            System.out.printf("ID: %d | %s (%s to %s) at %s\n",
                    concert.Id, concert.Name, concert.StartingDate, concert.EndingDate, concert.HallName);
        }

        System.out.print("Enter Concert ID to3 reserve: ");
        int concertId = Integer.parseInt(scanner.nextLine());

        List<SeatDTO> seats = orderRepository.getAvailableSeats(concertId);
        if (seats.isEmpty()) {
            System.out.println("No available seats for this concert.");
            return null;
        }

        System.out.println("Available Seats:");
        for (SeatDTO seat : seats) {
            System.out.printf("ID: %d | Row: %d, Number: %d\n", seat.Id, seat.Row, seat.Number);
        }

        System.out.print("Enter Seat ID to reserve: ");
        int seatId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter your Username for recipient: ");
        String loggedUsername= scanner.nextLine();
        int userId = userRepository.getUserIdByUsername(loggedUsername);

        boolean success = orderRepository.reserveSeat(userId, concertId, seatId);
        System.out.println(success ? "Reservation successful." : "Failed to reserve seat.");

        return null;
    }
}

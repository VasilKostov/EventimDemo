package controllers;

import interfaces.IOrderService;
import models.dtos.SeatDTO;
import services.OrderService;

import java.util.List;

public class OrderController {
    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    public void showAvailableSeats(int concertId) {
        List<SeatDTO> seats = orderService.getAvailableSeats(concertId);

        if (seats.isEmpty()) {
            System.out.println("No available seats for this concert.");
        } else {
            System.out.println("Available seats:");
            for (SeatDTO seat : seats) {
                System.out.printf("Seat id %d: Row %d, Number %d (Hall %d)%n",
                        seat.getId(), seat.getRow(), seat.getNumber(), seat.getHallId());
            }
        }
    }

    public void reserveSeat(int userId, int concertId, int seatId) {
        boolean success = orderService.reserveSeat(userId, concertId, seatId);
        if (success) {
            System.out.println("Seat reserved successfully!");
        } else {
            System.out.println("Failed to reserve seat. It may already be taken.");
        }
    }
}

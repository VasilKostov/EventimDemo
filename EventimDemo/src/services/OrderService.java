package services;

import interfaces.IOrderService;
import models.dtos.SeatDTO;
import repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<SeatDTO> getAvailableSeats(int concertId) {
        if(concertId < 0){
            return new ArrayList<>();
        }

        return orderRepository.getAvailableSeats(concertId);
    }

    @Override
    public boolean reserveSeat(int userId, int concertId, int seatId) {
        if(concertId < 0 || seatId < 0 || userId < 0){
            return false;
        }

        List<SeatDTO> available = orderRepository.getAvailableSeats(concertId);
        boolean isAvailable = available.stream().anyMatch(s -> s.getId() == seatId);

        if (!isAvailable) {
            return false;
        }

        return orderRepository.reserveSeat(userId, concertId, seatId);
    }
}

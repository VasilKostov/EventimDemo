package controllers;

import exceptions.ApplicationException;
import interfaces.IConcertRepository;
import interfaces.IHallRepository;
import interfaces.IOrderRepository;
import interfaces.IUserRepository;
import menus.LoginMenu;
import repositories.ConcertRepository;
import repositories.HallRepository;
import repositories.OrderRepository;
import repositories.UserRepository;
import singletons.UsersRoles;

import java.util.Scanner;

public class AppController {
    private final IHallRepository hallService = new HallRepository();
    private final IUserRepository userService = new UserRepository();
    private final IConcertRepository concertService = new ConcertRepository();
    private final IOrderRepository orderService = new OrderRepository();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        importDefaultRoles();
        importDefaultAdmin();
        importDefaultHalls();

        LoginMenu loginMenu = new LoginMenu(scanner, concertService, orderService, userService, hallService);
        loginMenu.start();
    }

    private void importDefaultRoles() {
        userService.importRoles();
    }

    private void importDefaultAdmin() {
        int adminId = userService.getUserIdByUsername("admin");

        if (adminId == -1) {
            userService.createUser("admin", "admin123", UsersRoles.Admin);
            System.out.println("Default admin user created (admin/admin123).");
        } else {
            System.out.println("Default admin user already exists.");
        }
    }

    private void importDefaultHalls() {
        int[] seatsPerRow1 = {3, 3, 3, 4, 3};
        hallService.importHallWithSeats("Arena Sofia", seatsPerRow1);

        int[] seatsPerRow2 = {3, 4, 3, 4, 3};
        hallService.importHallWithSeats("Zala 1 NDK", seatsPerRow2);
    }
}

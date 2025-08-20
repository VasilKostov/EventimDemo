package controllers;

import interfaces.IConcertService;
import interfaces.IHallService;
import menus.LoginMenu;
import repositories.ConcertRepository;
import repositories.HallRepository;
import repositories.OrderRepository;
import repositories.UserRepository;
import services.ConcertService;
import services.HallService;
import services.OrderService;
import services.UserService;
import singletons.UsersRoles;

import java.util.Scanner;

public class AppController {
    private final Scanner scanner = new Scanner(System.in);
    private final UserController userController = new UserController(new UserService(new UserRepository()));
    private final HallController hallController = new HallController(new HallService(new HallRepository()), scanner);
    private final int[] DEFAULT_SEATS = {3, 3, 3, 4, 3};

    public void start() {
        importDefaultRoles();
        importDefaultAdmin();
        importDefaultHalls();

        OrderController orderController = new OrderController(new OrderService(new OrderRepository()));
        ConcertController concertController = new ConcertController(new ConcertService(new ConcertRepository(), hallController));
        LoginMenu loginMenu = new LoginMenu(scanner, concertController, orderController, userController, hallController);
        loginMenu.start();
    }

    private void importDefaultRoles() {
        userController.importRoles();
    }

    private void importDefaultAdmin() {
        int adminId = userController.getUserId("admin");

        if (adminId == -1) {
            userController.createUser("admin", "admin123", UsersRoles.Admin);
            System.out.println("Default admin user created (admin/admin123).");
        } else {
            System.out.println("Default admin user already exists.");
        }
    }

    private void importDefaultHalls() {
        hallController.addHallWithSeats("Vasil Levski Stadium", DEFAULT_SEATS);
        hallController.addHallWithSeats("National Palace of Culture, Hall Number 1", DEFAULT_SEATS);
    }
}

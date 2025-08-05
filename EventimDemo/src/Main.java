import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import interfaces.IConcertService;
import interfaces.IHallService;
import interfaces.IOrderService;
import interfaces.IUserService;
import models.Concerts;
import models.Halls;
import models.dtos.ConcertDTO;
import models.dtos.HallDTO;
import models.dtos.SeatDTO;
import services.ConcertService;
import services.HallService;
import services.OrderService;
import services.UserService;
import Singletons.UsersRoles;
import db.DatabaseConnection;

public class Main {

    private static IHallService hallService = new HallService();
    private static IUserService userService = new UserService();
    private static IConcertService concertService = new ConcertService();
    private static IOrderService orderService = new OrderService();

    private static Scanner scanner = new Scanner(System.in);
    private static String loggedUsername = "";
    private static UsersRoles loggedUserRole = UsersRoles.Client;

    public static void main(String[] args) {
        ImportDefaultRoles();
        ImportDefaultAdmin();
        ImportDefaultHalls();

        boolean running = true;
        while (running) {
            System.out.println("\n=== Welcome ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    registerForm();
                    break;
                case "2":
                    if (loginForm()) {
                        mainMenu();
                    }
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }

        System.out.println("Goodbye!");
    }

    private static void mainMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n=== Mian Menu ===");
            System.out.println("Logged in as: " + loggedUsername + " (" + loggedUserRole + ")");
            System.out.println("1. View Concerts");
            if (loggedUserRole == UsersRoles.Admin) {
                System.out.println("2. Manage Concerts");
                System.out.println("3. Manage Halls");
                System.out.println("4. Logout");
            } else {
                System.out.println("2. Order Seat");
                System.out.println("3. Logout");
            }
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            if (loggedUserRole == UsersRoles.Admin) {
                switch (choice) {
                    case "1" -> listConcerts();
                    case "2" -> manageConcertsMenu();
                    case "3" -> manageHallsMenu();
                    case "4" -> loggedIn = false;
                    default -> System.out.println("Invalid choice.");
                }
            } else {
                switch (choice) {
                    case "1" -> listConcerts();
                    case "2" -> makeReservationForm();
                    case "3" -> loggedIn = false;
                    default -> System.out.println("Invalid choice.");
                }
            }
        }
        loggedUsername = "";
        loggedUserRole = UsersRoles.Client;
    }

    private static void registerForm() {
        System.out.println("--- Register ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            boolean res = userService.createUser(username, password);
            if (res) {
                System.out.println("User registered successfully! You can now login");
            } else {
                System.out.println("User registration failed.");
            }
        } catch (Exception e) {
            System.out.println("Failed to register user: " + e.getMessage());
        }
    }

    private static boolean loginForm() {
        System.out.println("==== Login ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (validateUser(username, password)) {
            loggedUsername = username;
            loggedUserRole = getUserRole(username);
            System.out.println("Login successful! Welcome, " + username);
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    private static boolean validateUser(String username, String password) {
        String sql = "SELECT id FROM users WHERE name = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static UsersRoles getUserRole(String username) {
        String sql = """
            SELECT r.name 
            FROM users u 
            JOIN usersToRoles ur ON u.id = ur.userId 
            JOIN roles r ON ur.roleId = r.id 
            WHERE u.name = ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                String roleName = rs.getString("name");
                return UsersRoles.valueOf(roleName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UsersRoles.Client;
    }

    private static void ImportDefaultRoles() {
        userService.importRoles();
    }

    private static void ImportDefaultAdmin() {
        try (Connection conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement("SELECT id FROM users WHERE name = ?")) {
            stmt.setString(1, "admin");
            var rs = stmt.executeQuery();
            if (!rs.next()) {
                userService.createUser("admin", "admin123", UsersRoles.Admin);
                System.out.println("Default admin user created (admin/admin123).");
            } else {
                System.out.println("Default admin user already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ImportDefaultHalls() {
        int[] seatsPerRow1 = {3, 3, 3, 4, 3};
        hallService.importHallWithSeats("Arena Sofia", seatsPerRow1);

        int[] seatsPerRow2 = {3, 4, 3, 4, 3};
        hallService.importHallWithSeats("Zala 1 NDK", seatsPerRow2);
    }

    private static void listConcerts() {
        List<ConcertDTO> concerts = concertService.getAllConcerts();
        System.out.println("\n=== Concerts ===");
        if (concerts.isEmpty()) {
            System.out.println("No concerts available :(");
            return;
        }
        for (ConcertDTO c : concerts) {
            System.out.printf("ID: %d | Name: %s | From: %s | To: %s | HallName: %s",
                    c.Id, c.Name, c.StartingDate, c.EndingDate, c.HallName);
        }
    }

    private static void manageConcertsMenu() {
        boolean managing = true;
        while (managing) {
            System.out.println("\n==== Manage Concerts ===");
            System.out.println("1. List concerts");
            System.out.println("2. Create concert");
            System.out.println("3. Update concert");
            System.out.println("4. Delete concert");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> listConcerts();
                case "2" -> createConcertForm();
                case "3" -> updateConcertForm();
                case "4" -> deleteConcertForm();
                case "5" -> managing = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void createConcertForm() {
        System.out.println("=== Create Concert ===");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Starting date (yyyy-mm-dd): ");
        Date startDate = Date.valueOf(scanner.nextLine());

        System.out.print("Ending date (yyyy-mm-dd): ");
        Date endDate = Date.valueOf(scanner.nextLine());
        int hallId = selectHallByNameAndDateRange(startDate, endDate);
        if (hallId == -1) {
            System.out.println("No hall selected. Concert creation cancelled.");
            return;
        }

        Concerts concert = new Concerts(name, startDate, endDate, hallId);

        boolean created = concertService.createConcert(concert);
        System.out.println(created ? "Concert created." : "Failed to create concert.");
    }

    private static void updateConcertForm() {
        System.out.print("Enter Concert ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        ConcertDTO existing = concertService.getConcertById(id);
        if (existing == null) {
            System.out.println("Concert not found.");
            return;
        }

        System.out.print("Name (" + existing.Name + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) existing.Name = name;

        System.out.print("Starting date (" + existing.StartingDate + ") yyyy-mm-dd: ");
        String startDateStr = scanner.nextLine();
        Date startDate = existing.StartingDate;
        if (!startDateStr.isBlank()) startDate = Date.valueOf(startDateStr);

        System.out.print("Ending date (" + existing.EndingDate + ") yyyy-mm-dd: ");
        String endDateStr = scanner.nextLine();
        Date endDate = existing.EndingDate;
        if (!endDateStr.isBlank())
            endDate = Date.valueOf(endDateStr);

        System.out.println("Current hall: " + existing.HallName);
        System.out.print("Change hall? (yes/no): ");
        String changeHall = scanner.nextLine();
        if (changeHall.equalsIgnoreCase("yes")) {
            int hallId = selectHallByNameAndDateRange(startDate, endDate);
            if (hallId != -1) {
                existing.HallId = hallId;
            } else {
                System.out.println("Hall not changed.");
            }
        }
        existing.StartingDate = startDate;
        existing.EndingDate = endDate;

        boolean updated = concertService.updateConcert(new Concerts(existing.Id, existing.Name, existing.StartingDate, existing.EndingDate, existing.HallId));
        System.out.println(updated ? "Concert updated." : "Failed to update concert.");
    }

    private static void deleteConcertForm() {
        System.out.print("Enter Concert ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        boolean deleted = concertService.deleteConcert(id);
        System.out.println(deleted ? "Concert deleted." : "Failed to delete concert.");
    }

    private static void manageHallsMenu() {
        boolean managing = true;
        while (managing) {
            System.out.println("\n=== Manage Halls ===");
            System.out.println("1. List halls");
            System.out.println("2. Create hall");
            System.out.println("3. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> listHalls();
                case "2" -> createHallForm();
                case "3" -> managing = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void listHalls() {
        List<Halls> halls = hallService.getAllHalls();
        System.out.println("\n=== Halls ===");
        if (halls.isEmpty()) {
            System.out.println("No halls found.");
            return;
        }
        for (Halls h : halls) {
            System.out.printf("ID: %d | Name: %s%n", h.Id, h.Name);
        }
    }

    private static void createHallForm() {
        System.out.print("Enter Hall name: ");
        String name = scanner.nextLine();

        System.out.print("Enter number of rows: ");
        int rows = Integer.parseInt(scanner.nextLine());
        int[] seatsPerRow = new int[rows];

        for (int i = 0; i < rows; i++) {
            System.out.print("Enter number of seats in row " + (i + 1) + ": ");
            seatsPerRow[i] = Integer.parseInt(scanner.nextLine());
        }

        hallService.importHallWithSeats(name, seatsPerRow);
        System.out.println("Hall created.");
    }

    private static int selectHallByNameAndDateRange(Date startDate, Date endDate) {
        List<HallDTO> halls = hallService.getAvailableHalls(startDate, endDate);

        if (halls.isEmpty()) {
            System.out.println("No halls available in the selected date range.");
            return -1;
        }

        while (true) {
            System.out.print("Search hall by name (or type 'list' to see all, 'cancel' to exit): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) return -1;

            List<HallDTO> filteredHalls;
            if (input.equalsIgnoreCase("list") || input.isBlank()) {
                filteredHalls = halls;
            } else {
                filteredHalls = halls.stream()
                        .filter(h -> h.getName().toLowerCase().contains(input.toLowerCase()))
                        .toList();
            }

            if (filteredHalls.isEmpty()) {
                System.out.println("No halls found matching '" + input + "'");
                continue;
            }

            System.out.println("Available halls:");
            for (HallDTO hall : filteredHalls) {
                System.out.printf("ID: %d, Name: %s%n", hall.getId(), hall.getName());
            }

            System.out.print("Enter hall ID to select (or 'cancel' to exit): ");
            String hallIdInput = scanner.nextLine().trim();

            if (hallIdInput.equalsIgnoreCase("cancel")) return -1;

            try {
                int hallId = Integer.parseInt(hallIdInput);
                boolean exists = filteredHalls.stream().anyMatch(h -> h.getId() == hallId);
                if (exists) return hallId;
                else System.out.println("Invalid hall ID selected.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric hall ID.");
            }
        }
    }

    private static void makeReservationForm() {
        System.out.println("=== Concert Reservation ===");

        List<ConcertDTO> concerts = concertService.getAllConcerts();
        if (concerts.isEmpty()) {
            System.out.println("No concerts available.");
            return;
        }

        System.out.println("Available Concerts:");
        for (ConcertDTO concert : concerts) {
            System.out.printf("ID: %d | %s (%s to %s) at %s\n",
                    concert.Id, concert.Name, concert.StartingDate, concert.EndingDate, concert.HallName);
        }

        System.out.print("Enter Concert ID to3 reserve: ");
        int concertId = Integer.parseInt(scanner.nextLine());

        List<SeatDTO> seats = orderService.getAvailableSeats(concertId);
        if (seats.isEmpty()) {
            System.out.println("No available seats for this concert.");
            return;
        }

        System.out.println("Available Seats:");
        for (SeatDTO seat : seats) {
            System.out.printf("ID: %d | Row: %d, Number: %d\n", seat.Id, seat.Row, seat.Number);
        }

        System.out.print("Enter Seat ID to reserve: ");
        int seatId = Integer.parseInt(scanner.nextLine());
        int userId = userService.getUserIdByUsername(loggedUsername);

        boolean success = orderService.reserveSeat(userId, concertId, seatId);
        System.out.println(success ? "Reservation successful." : "Failed to reserve seat.");
    }

}

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import controllers.AppController;
import exceptions.ApplicationException;
import interfaces.IConcertRepository;
import interfaces.IHallRepository;
import interfaces.IOrderRepository;
import interfaces.IUserRepository;
import menus.LoginMenu;
import models.Concerts;
import models.Halls;
import models.dtos.ConcertDTO;
import models.dtos.HallDTO;
import models.dtos.SeatDTO;
import repositories.ConcertRepository;
import repositories.HallRepository;
import repositories.OrderRepository;
import repositories.UserRepository;
import singletons.UsersRoles;

public class Main {
    public static void main(String[] args) {
        try {
            AppController app = new AppController();
            app.start();
        }
        catch (ApplicationException e) {
            System.out.println("We ran into an error. Please try again.");
        }
    }
}

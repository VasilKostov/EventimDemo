package singletons;

import interfaces.ICommand;
import interfaces.IConcertService;
import interfaces.IHallService;
import interfaces.IMenu;
import models.dtos.HallDTO;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Helper {

    public static void executeCommand(Map<Integer, ICommand> commands, int choice){
        ICommand command = commands.get(choice);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid choice");
        }
    }

    public static void executeCommandWithMenu(Map<Integer, ICommand> commands, int choice){
        ICommand command = commands.get(choice);
        if (command != null) {
            IMenu menu = command.execute();
            if(menu != null){
                menu.start();
            }
        } else {
            System.out.println("Invalid choice");
        }
    }
}

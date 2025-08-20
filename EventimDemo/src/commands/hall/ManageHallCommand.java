package commands.hall;

import controllers.HallController;
import interfaces.*;
import menus.HallMenu;

import java.util.Scanner;

public class ManageHallCommand implements ICommand {
    private final Scanner scanner;
    private final HallController hallController;

    public ManageHallCommand(Scanner scanner, HallController hallController) {
        this.scanner = scanner;
        this.hallController = hallController;
    }

    @Override
    public IMenu execute() {
        return new HallMenu(scanner, hallController);
    }
}

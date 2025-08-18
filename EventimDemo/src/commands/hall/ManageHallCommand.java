package commands.hall;

import interfaces.*;
import menus.HallMenu;

import java.util.Scanner;

public class ManageHallCommand implements ICommand {
    private final Scanner scanner;
    private final IHallRepository hallRepository;

    public ManageHallCommand(Scanner scanner, IHallRepository hallRepository) {
        this.scanner = scanner;
        this.hallRepository = hallRepository;
    }

    @Override
    public IMenu execute() {
        return new HallMenu(scanner, hallRepository);
    }
}

package commands.hall;

import controllers.HallController;
import interfaces.ICommand;
import interfaces.IHallService;
import interfaces.IMenu;
import models.Halls;

import java.util.List;

public class ListHallsCommand implements ICommand {
    private final HallController hallController;

    public ListHallsCommand(HallController hallController) {
        this.hallController = hallController;
    }

    @Override
    public IMenu execute() {
        System.out.println("\n=== Halls ===");

        hallController.showAllHalls();

        return null;
    }
}

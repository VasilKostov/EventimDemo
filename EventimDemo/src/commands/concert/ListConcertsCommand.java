package commands.concert;

import controllers.ConcertController;
import interfaces.ICommand;
import interfaces.IConcertService;
import interfaces.IMenu;
import models.dtos.ConcertDTO;

import java.util.List;

public class ListConcertsCommand implements ICommand {
    private final ConcertController concertController;

    public ListConcertsCommand(ConcertController concertController) {
        this.concertController = concertController;
    }

    @Override
    public IMenu execute() {
        System.out.println("\n=== Concerts ===");

        concertController.showAllConcerts();

        return null;
    }
}

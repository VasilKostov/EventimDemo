package commands.concert;

import controllers.ConcertController;
import interfaces.ICommand;
import interfaces.IConcertService;
import interfaces.IMenu;
import models.dtos.ConcertDTO;

import javax.print.MultiDocPrintService;
import java.util.List;

public class ViewConcertsCommand implements ICommand {
    private final ConcertController concertController;

    public ViewConcertsCommand(ConcertController concertController) {
        this.concertController = concertController;
    }

    @Override
    public IMenu execute(){
        System.out.println("\n=== Concerts ===");

        concertController.showAllConcerts();

        return null;
    }
}

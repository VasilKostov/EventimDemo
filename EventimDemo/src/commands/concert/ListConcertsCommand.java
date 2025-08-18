package commands.concert;

import interfaces.ICommand;
import interfaces.IConcertRepository;
import interfaces.IMenu;
import models.dtos.ConcertDTO;

import java.util.List;

public class ListConcertsCommand implements ICommand {
    private final IConcertRepository concertRepository;

    public ListConcertsCommand(IConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Override
    public IMenu execute() {
        List<ConcertDTO> concerts = concertRepository.getAllConcerts();
        System.out.println("\n=== Concerts ===");
        if (concerts.isEmpty()) {
            System.out.println("No concerts available :(");

            return null;
        }

        for (ConcertDTO c : concerts) {
            System.out.printf("ID: %d | Name: %s | From: %s | To: %s | HallName: %s%n",
                    c.Id, c.Name, c.StartingDate, c.EndingDate, c.HallName);
        }

        return null;
    }
}

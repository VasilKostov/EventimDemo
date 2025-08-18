package commands.hall;

import interfaces.ICommand;
import interfaces.IHallRepository;
import interfaces.IMenu;
import models.Halls;

import java.util.List;

public class ListHallsCommand implements ICommand {
    private final IHallRepository hallRepository;

    public ListHallsCommand(IHallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @Override
    public IMenu execute() {
        List<Halls> halls = hallRepository.getAllHalls();

        System.out.println("\n=== Halls ===");

        if (halls.isEmpty()) {
            System.out.println("No halls found.");

            return null;
        }
        for (Halls h : halls) {
            System.out.printf("ID: %d | Name: %s%n", h.Id, h.Name);
        }

        return null;
    }
}

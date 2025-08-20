package models.dtos;

public class SeatDTO {
    private final int id;
    private final int row;
    private final int number;
    private final int hallId;

    public SeatDTO(int id, int row, int number, int hallId) {
        this.id = id;
        this.row = row;
        this.number = number;
        this.hallId = hallId;
    }

    public int getId() {
        return id;
    }

    public int getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    public int getHallId() {
        return hallId;
    }

    @Override
    public String toString() {
        return "Seat " + number + " in row " + row;
    }
}

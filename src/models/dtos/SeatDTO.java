package models.dtos;

public class SeatDTO {
    public int Id;
    public int Row;
    public int Number;
    public int HallId;

    public SeatDTO(int id, int row, int number, int hallId) {
        this.Id = id;
        this.Row = row;
        this.Number = number;
        this.HallId = hallId;
    }

    @Override
    public String toString() {
        return "Seat " + Number + " in row " + Row;
    }
}

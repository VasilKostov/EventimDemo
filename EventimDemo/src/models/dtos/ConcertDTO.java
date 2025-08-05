package models.dtos;

public class ConcertDTO {
    public int Id;
    public String Name;
    public java.sql.Date StartingDate;
    public java.sql.Date EndingDate;
    public String HallName;
    public int HallId;

    public ConcertDTO(int id, String name, java.sql.Date startingDate, java.sql.Date endingDate, String hallName, int hallId) {
        Id = id;
        Name = name;
        StartingDate = startingDate;
        EndingDate = endingDate;
        HallName = hallName;
        HallId = hallId;
    }
}

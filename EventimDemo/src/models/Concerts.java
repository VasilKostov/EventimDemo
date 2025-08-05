package models;

import java.sql.Date;

public class Concerts {
    public Concerts(int id, String name, Date startDate, Date endDate, int hallId) {
        Id = id;
        Name = name;
        StartingDate = startDate;
        EndingDate = endDate;
        HallId = hallId;
    }

    public Concerts(String name, Date startDate, Date endDate, int hallId) {
        Name = name;
        StartingDate = startDate;
        EndingDate = endDate;
        HallId = hallId;
    }
    public int Id;
    public String Name;
    public Date StartingDate;
    public Date EndingDate;
    public int HallId;
}

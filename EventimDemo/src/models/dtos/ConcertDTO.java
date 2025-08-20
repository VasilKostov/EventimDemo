package models.dtos;

import java.sql.Date;

public class ConcertDTO {
    private int id;
    private String name;
    private Date startingDate;
    private Date endingDate;
    private String hallName;
    private int hallId;

    public ConcertDTO(int id, String name, Date startingDate, Date endingDate, String hallName, int hallId) {
        this.id = id;
        this.name = name;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.hallName = hallName;
        this.hallId = hallId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public String getHallName() {
        return hallName;
    }

    public int getHallId() {
        return hallId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | From: %s | To: %s | Hall: %s",
                id, name, startingDate, endingDate, hallName
        );
    }
}

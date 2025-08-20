package models;

public class Halls {
    public int Id;
    public String Name;

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setName(String name) {
        this.Name = name;
    }

    @Override
    public String toString() {
        return "Hall: " + getName() + " (ID: " + getId() + ")";
    }
}

package domain;
import repository.ICrudRepository;
import java.util.List;

public class Attraction{
    public String name;
    private final Integer capacity;
    public Instructor instructor;
    public List<Guest> guestList;
    public double price;
    public String location;

    Weekday day;

    public Attraction(String name, Integer capacity, Instructor instructor, List<Guest> guestList, double price, String location, Weekday day) {
        this.name = name;
        this.capacity = capacity;
        this.instructor = instructor;
        this.guestList = guestList;
        this.price = price;
        this.location = location;
        this.day = day;
    }
    public Integer getCapacity() {
        return capacity;
    }

    public List<Guest> getGuests()
    {
        return guestList;
    }
}

package domain;
import repository.ICrudRepository;
import java.util.List;
import java.util.UUID;

public class Attraction{
    public String name;
    private final Integer capacity;
    public Instructor instructor;
    public List<Guest> guestList;
    public double price;
    public String location;

    private String ID;
    public Weekday day;

    public Attraction(String name, Integer capacity, Instructor instructor, List<Guest> guestList, double price, String location, Weekday day) {
        this.name = name;
        this.capacity = capacity;
        this.instructor = instructor;
        this.guestList = guestList;
        this.price = price;
        this.location = location;
        this.day = day;
        this.ID = this.name.substring(0,1)+this.location.substring(0,1)+day.toString();
    }
    public Integer getCapacity() {
        return capacity;
    }

    public List<Guest> getGuests()
    {
        return guestList;
    }

    public String getID(){
        return this.ID;
    }

    public int getNrOfGuests(){
        return this.guestList.size();
    }

    public int getNrOfFreePlaces(){
        return this.capacity - this.guestList.size();
    }
    public void showAttractionMenu(){

    }
}

package domain;

import java.util.List;
import java.util.UUID;

public class Attraction implements Comparable<Attraction>{
    private String ID;
    public String name;
    private final Integer capacity;
    public Instructor instructor;
    public List <Guest> guestList;
    public double price;
    public String location;
    public Weekday day;

    public Attraction(String name, Integer capacity, Instructor instructor, List<Guest> guestList, double price, String location, Weekday day) {
        this.name = name;
        this.capacity = capacity;
        this.instructor = instructor;
        this.guestList = guestList;
        this.price = price;
        this.location = location;
        this.day = day;
        // create ID
        this.ID = UUID.randomUUID().toString();
    }

    public Integer getCapacity() {
        return capacity;
    }
    public String  getID() {
        return ID;
    }

    public Integer getNrOfGuests(){
        return this.guestList.size();
    }

    public Integer getNrOfFreePlaces(){
        return this.capacity - this.getNrOfGuests();
    }

    @Override
    public int compareTo(Attraction o) {
        return this.name.compareTo(o.name);
    }
}

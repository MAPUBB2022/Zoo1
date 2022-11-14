package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Attraction implements Comparable<Attraction>{
    private String ID;
    public String name;
    private final Integer capacity;
    private Instructor instructor;
    public List<Guest> guestList;
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

    public Attraction(String name, Integer capacity, Instructor instructor, double price, String location, Weekday day) {
        this.name = name;
        this.capacity = capacity;
        this.instructor = instructor;
        // initially no guests
        this.guestList = new ArrayList<Guest>();
        this.price = price;
        this.location = location;
        this.day = day;
        this.ID = UUID.randomUUID().toString();
    }

    public Attraction(Attraction attr){
        this.name = attr.name;
        this.capacity = attr.capacity;
        this.instructor = attr.instructor;
        this.guestList = attr.guestList;
        this.price = attr.price;
        this.location = attr.location;
        this.day = attr.day;
        this.ID = attr.ID;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String  getID() {
        return ID;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Integer getNrOfGuests(){
        return this.guestList.size();
    }

    public Integer getNrOfFreePlaces(){
        return this.capacity - this.getNrOfGuests();
    }

    public void addGuest(Guest guest) {this.guestList.add(guest);}

    @Override
    public int compareTo(Attraction o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "   ID=" + ID +
                "  name=" + name +
                "  capacity=" + capacity +
                "  instructor=" + instructor.getData() +
                "  price=" + price +
                "  location=" + location +
                "  day=" + day +
                '\n';
    }
}

package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Attraction implements Comparable<Attraction>{
    @Id
    private String ID;
    public String name;
    private Integer capacity;
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "attraction_guests",
            joinColumns = @JoinColumn(name ="attraction_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id"))
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

    public Attraction() {}

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
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
        // this.ID = UUID.randomUUID().toString();
        this.ID = this.name.substring(0,1)+this.location.substring(0,1)+ '-' +this.day.toString().substring(0,3);
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
        //this.ID = UUID.randomUUID().toString();
        this.ID = this.name.substring(0,1)+this.location.substring(0,1)+ '-' +this.day.toString().substring(0,3);
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
        return  " ID=" + ID +
                "  \tname=" + name +
                "  \tcapacity=" + capacity +
                "  \tinstructor=" + instructor.getData() +
                "  \tprice=" + price +
                "  \tlocation=" + location +
                "  \tday=" + day +
                "  \tfree places=" + this.getNrOfFreePlaces() +
                "  \tsigned up guests=" + this.getNrOfGuests() +
                '\n';
    }
}

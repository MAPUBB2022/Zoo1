package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Instructor extends Person{

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor_id")
    private List<Attraction> attractions;

    public Instructor(String id, String firstName, String lastName, String password, List<Attraction> attractionsOfInstructor, double sum) {
        super(id, firstName, lastName, password, sum);
        this.attractions = attractionsOfInstructor;
        calculateSum();
    }

    public Instructor(String id, String firstName, String lastName, String password) {
        super(id, firstName, lastName, password);
        this.attractions = new ArrayList<Attraction>();
    }

    public Instructor(){}

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    public void addAttraction(Attraction attraction){
        this.attractions.add(attraction);
        calculateSum();
    }

    public void removeAttraction(Attraction attraction){
        this.attractions.remove(attraction);
        calculateSum();
    }

    @Override
    String getData() {
        return "Name: " + firstName + ' ' + lastName;
    }

    @Override
    public void calculateSum() {
        this.finalSum = 0;
        for (Attraction a: this.attractions){
            finalSum += a.price * a.getNrOfGuests();
        }
    }

    @Override
    public String toString() {
        String attractions_name = "";
        for (Attraction a: this.attractions)
            attractions_name = attractions_name + "(" + a.name + " " + a.day + ", " + a.location + "), ";

        if (attractions_name.length() > 0)
            attractions_name = attractions_name.substring(0, attractions_name.length() - 2);
        return  "  ID=" + ID + '\'' +
                "  firstName=" + firstName + '\'' +
                "  lastName=" + lastName + '\'' +
                "  finalSumFromGuests=" + finalSum +
                "  hält: " + attractions_name +
                '\n';
    }
}

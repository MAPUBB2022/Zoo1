package domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Guest extends Person implements Comparable<Guest>{
    private LocalDate birthday;
    @ManyToMany(mappedBy = "guestList")
    List<Attraction>  attractions;

    public Guest(String username, String firstName, String lastName, String password, LocalDate birthday, double finalSum, List<Attraction> attractions) {
        super(username, firstName, lastName, password, finalSum);
        this.birthday = birthday;
        this.attractions = attractions;
    }

    public Guest(String username, String firstName, String lastName, String password, LocalDate birthday) {
        super(username, firstName, lastName, password);
        this.birthday = birthday;
        this.attractions = new ArrayList<Attraction>();
    }

    public Guest(){}

    @Override
    public String getData() {
        String attractions_name = "";
        for (Attraction a: attractions)
            attractions_name = attractions_name + '(' + a.name + " " + a.day + ", " + a.getInstructor().getName() + "), ";

        String data = "ID: " + ID + ", Name: " + firstName + ' ' + lastName + ", Alter: " + this.getAge() +'\n';
        if (!attractions_name.equals("")){
            attractions_name = attractions_name.substring(0, attractions_name.length() - 2);
            data = data + "nimmt teil an: " + attractions_name + ".\n";
        }
        return data;
    }

    public Integer getAge() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }
    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void addAttraction(Attraction attraction){
        this.attractions.add(attraction);
        calculateSum();
    }

    public void removeAttraction(Attraction attraction){
        this.attractions.remove(attraction);
        calculateSum();
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public void calculateSum() {
        int age = this.getAge();
        double sum = 0.0;
        for (Attraction a: attractions){
            sum += a.price;
        }
        if (age > 60){
            setFinalSum(sum * 0.8);
        }
        else if (age < 18)
        {
            setFinalSum(sum * 0.5);
        }
        else
            setFinalSum(sum);
    }

    @Override
    public int compareTo(Guest o) {
        if (this.finalSum > o.finalSum) return 1;
        else if (this.finalSum == o.finalSum) return 0;
        else return -1;
    }

    @Override
    public String toString() {
        return "  ID=" + ID  +
                "  finalSum=" + finalSum +
                "  firstName=" + firstName +
                "  lastName=" + lastName +
                '\n';
    }
}

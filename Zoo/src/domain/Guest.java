package domain;

import repository.OperationsInterface1;
import repository.OperationsInterface2;
import repository.OperationsInterface3;

import java.time.LocalDate;

import java.time.Period;
import java.util.List;

public class Guest extends Person implements Comparable<Guest>, OperationsInterface1, OperationsInterface2<Guest>, OperationsInterface3 {
    private final LocalDate birthday;

    public Guest(String username, String firstName, String lastName, String password, LocalDate birthday, double finalSum, List<Attraction> attractions) {
        super(username, firstName, lastName, password, attractions, finalSum);
        this.birthday = birthday;
    }

    public Guest(String username, String firstName, String lastName, String password, LocalDate birthday) {
        super(username, firstName, lastName, password);
        this.birthday = birthday;
    }

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


    @Override
    public void iterateThroughList() {
        // returns a list in a such way: 1. AttractionName1, 2. AttractionName2 ...
        OperationsInterface1 operationsInterface1;
        operationsInterface1 = ()->attractions.forEach(attraction -> attraction.name = new StringBuilder().append((attractions.indexOf(attraction))).append(attraction.name).toString());
    }

    @Override
    public boolean hasAttractionOrNot(Guest guest) {  // true if has
        OperationsInterface2 operationsInterface2;
        operationsInterface2 = (a) -> {return attractions.isEmpty() == false;};
        return operationsInterface2.hasAttractionOrNot(guest);
    }

    @Override
    public boolean ExistsOrNot() { // has attractions to participate on -> true
        OperationsInterface3 operationsInterface3;
        operationsInterface3 = ()->{return this.attractions.isEmpty() == false;};
        return operationsInterface3.ExistsOrNot();
    }
}
package domain;

import repository.OperationsInterface3;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends Person implements OperationsInterface3 {

    public Instructor(String id, String firstName, String lastName, String password, List<Attraction> attractionsOfInstructor, double sum) {
        super(id, firstName, lastName, password, attractionsOfInstructor, sum);
        calculateSum();
    }

    public Instructor(String id, String firstName, String lastName, String password) {
        super(id, firstName, lastName, password);
    }

    @Override
    protected String getData() {
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

    @Override
    public boolean ExistsOrNot() {
        // has attractions to hold -> true
        OperationsInterface3 operationsInterface3;
        operationsInterface3 = ()->{return this.getAttractions().isEmpty() == false;};
        return operationsInterface3.ExistsOrNot();
    }
}
package domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class Guest extends Person implements Comparable<Guest>{
    private final LocalDate birthday;
    private double finalSum;
    public List<Attraction> attractions;

    public Guest(String firstName, String lastName, LocalDate birthday, double finalSum, List<Attraction> attractions) {
        super(firstName, lastName);
        this.birthday = birthday;
        this.finalSum = finalSum;
        this.attractions = attractions;
    }

    @Override
    String getData() {
        String attractions_name = "";
        for (Attraction a: attractions)
            attractions_name = attractions_name + a.name + " " + a.day + ", ";

        attractions_name.substring(0, attractions_name.length() - 2);
        return "ID: " + ID + ", Name: " + firstName + ' ' + lastName + ", Alter: " + this.getAge() +
                "\nnimmt teil an: " + attractions_name + '.';
    }

    public Integer getAge() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }

    public double getFinalSum() {
        return finalSum;
    }

    public void setFinalSum(double finalSum) {
        this.finalSum = finalSum;
    }

    public void calculateSum(){
        // calculate_age(), esetleg switch
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
        if (this.finalSum < o.finalSum) return 1;
        else if (this.finalSum == o.finalSum) return 0;
        else return -1;

    }
}

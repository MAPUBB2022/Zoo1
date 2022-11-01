package domain;

import repository.GuestRepository;
import repository.ICrudRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class Guest extends Person {
    private final LocalDate birthday;
    private double finalSum;
    public List<Attraction> attractions;

    public Guest(String firstName, String lastName, Integer guestID, LocalDate birthday, double finalSum, List<Attraction> attractions) {
        super(firstName, lastName, guestID);
        this.birthday = birthday;
        this.finalSum = finalSum;
        this.attractions = attractions;
    }

    public Integer getAge(){
        return Period.between(this.birthday,LocalDate.now()).getYears();
    }
    public double getFinalSum() {
        return finalSum;
    }

    public void setFinalSum(double finalSum) {
        this.finalSum = finalSum;
    }

    @Override
    String getData() {
        String attractionName = "";
        int age = this.getAge();
        for(Attraction a: attractions)
        {
            attractionName = attractionName + a.name + " " + a.day + ", ";
        }
        attractionName.substring(0, attractionName.length() - 2);
        return "ID: " + ID + ", Name:" + firstName + " " + lastName + ", Alter: " + age + "\n" + "nimmt teil an: " + attractionName + "." ;
    }
    public void calculateSum(){
        int age = this.getAge();
        double sum = 0;
        for(Attraction a: attractions)
        {
            sum += a.price;
        }
        if(age >= 60) {
            setFinalSum(sum*0.8);
        }
        else if (age < 18){
            setFinalSum(sum*0.5);
        }
        else setFinalSum(sum);
    }
}

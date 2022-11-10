package domain;

import java.util.List;

public class Instructor extends Person{
    public List<Attraction>  attractionsOfInstructor;
    private double finalSumFromGuests;

    public Instructor(List<Attraction> attractionsOfInstructor, String firstName, String lastName) {
        super(firstName, lastName);
        this.attractionsOfInstructor = attractionsOfInstructor;
        calculateSum();
    }

    public void calculateSum(){
        this.finalSumFromGuests = 0;
        for (Attraction a: attractionsOfInstructor){
            finalSumFromGuests += a.price;
        }
    }
    @Override
    String getData() {
        String attractions_name = "";
        for (Attraction a: attractionsOfInstructor)
            attractions_name = attractions_name + a.name + " " + a.day + ", ";

        attractions_name.substring(0, attractions_name.length() - 2);
        return "Name: " + firstName + ' ' + lastName +
                "\n hält: " + attractions_name + '.';
    }



}

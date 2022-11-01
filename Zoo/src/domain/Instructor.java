package domain;
import repository.ICrudRepository;
import java.util.List;

public class Instructor extends Person {
    public List<Attraction> attractionOfInstructor;

    public Instructor(List<Attraction> attractionOfInstructor, String firstName, String lastName, Integer ID) {
        super(firstName,lastName, ID);
        this.attractionOfInstructor = attractionOfInstructor;
    }

    @Override
    String getData() {
        String attractionName = "";
        for(Attraction a: attractionOfInstructor)
        {
            attractionName = attractionName + a.name + " " + a.day + ", ";
        }
        attractionName.substring(0, attractionName.length() - 2);
        return "Name:" + firstName + " " + lastName + "\n" + "hält: " + attractionName + "." ;
    }

}

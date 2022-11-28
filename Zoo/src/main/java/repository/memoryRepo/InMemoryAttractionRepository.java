package repository.memoryRepo;

import domain.Attraction;
import domain.Guest;
import domain.Instructor;
import domain.Weekday;
import repository.AttractionRepository;
import repository.InstructorRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAttractionRepository implements AttractionRepository {

    private final List<Attraction> allAttractions;
    private final InstructorRepository instructorRepository;
    public InMemoryAttractionRepository(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
        this.allAttractions = new ArrayList<Attraction>();
        this.populateAttractions();
    }

    private void populateAttractions(){
        List<Instructor> instructors = this.instructorRepository.getAllInstructors();
        Instructor instructor1 = instructors.get(0);
        Instructor instructor2 = instructors.get(1);
        Instructor instructor3 = instructors.get(2);
        Instructor instructor4 = instructors.get(3);
        Instructor instructor5 = instructors.get(4);
        Instructor instructor6 = instructors.get(5);

        Attraction attraction1 = new Attraction("Zoo time",100, instructor1,180.99, "A456", Weekday.MONDAY);
        Attraction attraction2 = new Attraction("Elephantastic",200, instructor2,99.99,"B456", Weekday.TUESDAY);
        Attraction attraction3 = new Attraction("Fluffy Animals",10, instructor3,55.00,"A456", Weekday.WEDNESDAY);
        Attraction attraction4 = new Attraction("Angry Panda",17, instructor4,70.00,"X588", Weekday.THURSDAY);
        Attraction attraction5 = new Attraction("White Lion",8, instructor5,250.00,"F444", Weekday.FRIDAY);
        Attraction attraction6 = new Attraction("White Lion",80, instructor2,170.89,"T545", Weekday.SATURDAY);
        Attraction attraction7 = new Attraction("White Lion",55, instructor4,60.50,"RQ67", Weekday.SUNDAY);
        Attraction attraction8 = new Attraction("VIP zoo show",10, instructor6,300.87,"BRT60", Weekday.WEDNESDAY);

        instructor1.addAttraction(attraction1);
        instructor2.addAttraction(attraction2);
        instructor3.addAttraction(attraction3);
        instructor4.addAttraction(attraction4);
        instructor5.addAttraction(attraction5);
        instructor2.addAttraction(attraction6);
        instructor4.addAttraction(attraction7);
        instructor6.addAttraction(attraction8);

        this.add(attraction1);
        this.add(attraction2);
        this.add(attraction3);
        this.add(attraction4);
        this.add(attraction5);
        this.add(attraction6);
        this.add(attraction7);
        this.add(attraction8);
    }

    @Override
    public List<Attraction> getAllAttractions() {
        return allAttractions;
    }

    @Override
    public void add(Attraction attraction) {
        for (Attraction attr: this.allAttractions){
            if (attr.getID().equals(attraction.getID())){
                System.out.println("Attraction with this ID already exists");
                return;
            }
        }
        this.allAttractions.add(attraction);
    }

    @Override
    public void delete(String id) {
        Attraction attraction = this.findByID(id);
        this.allAttractions.remove(attraction);
    }

    @Override
    public void update(String id, Attraction attraction) {
        Attraction attr = this.findByID(id);
        int position = this.allAttractions.indexOf(attr);
        this.allAttractions.set(position,attraction);
    }

    @Override
    public Attraction findByID(String id) {
        for (Attraction attraction: this.allAttractions){
            if (attraction.getID().equals(id))
                return attraction;
        }
        return null;
    }
}
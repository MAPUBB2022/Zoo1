package repository.memoryRepo;

import domain.Attraction;
import domain.Instructor;
import repository.InstructorRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryInstructorRepository implements InstructorRepository {
    private List<Instructor> allInstructors;

    public InMemoryInstructorRepository(List<Instructor> allInstructors) {
        this.allInstructors = new ArrayList<>();
        this.populateInstructors();
    }

    private void populateInstructors(){
        List<Attraction> a1 = null;
        List<Attraction> a2 = null;
        List<Attraction> a3 = null;
        List<Attraction> a4 = null;
        List<Attraction> a5 = null;
        Instructor instructor1 = new Instructor(a1,"James", "Parker");
        Instructor instructor2 = new Instructor(a2, "James", "John");
        Instructor instructor3 = new Instructor(a3, "Misterious", "Lucy");
        Instructor instructor4 = new Instructor(a4, "Gal", "Katy");
        Instructor instructor5 = new Instructor(a5, "Pop", "Camila");
        this.add(instructor1);
        this.add(instructor2);
        this.add(instructor3);
        this.add(instructor4);
        this.add(instructor5);
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return allInstructors;
    }

    @Override
    public void add(Instructor instructor) {
        for (Instructor instr: this.allInstructors){
            if (instr.getID().equals(instructor.getID())){
                System.out.println("Instructor with this ID already exists");
                return;
            }
        }
        this.allInstructors.add(instructor);
    }
    @Override
    public Instructor findByID(String id) {
        for (Instructor instructor: this.allInstructors){
            if(instructor.getID().equals(id))
                return instructor;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        Instructor instructor = this.findByID(id);
        this.allInstructors.remove(instructor);
    }

    @Override
    public void update(Instructor instructor, String id) {
        Instructor instr = this.findByID(id);
        int position = this.allInstructors.indexOf(instr);
        this.allInstructors.set(position, instructor);
    }


}
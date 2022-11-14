package repository.memoryRepo;

import domain.Attraction;
import domain.Instructor;
import repository.InstructorRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryInstructorRepository implements InstructorRepository {
    private final List<Instructor> allInstructors;

    public InMemoryInstructorRepository() {
        this.allInstructors = new ArrayList<>();
        this.populateInstructors();
    }

    private void populateInstructors(){
        Instructor instructor1 = new Instructor("i1","James", "Parker", "123456");
        Instructor instructor2 = new Instructor("i2","James", "John", "qwerty");
        Instructor instructor3 = new Instructor("i3", "Lucy", "Misterious", "abc123");
        Instructor instructor4 = new Instructor("i4", "Katy", "Gal", "123321");
        Instructor instructor5 = new Instructor("i5","Camila", "Pop", "password1");
        Instructor instructor6 = new Instructor("i6","Mircea", "Miron", "abcd1234");

        this.add(instructor1);
        this.add(instructor2);
        this.add(instructor3);
        this.add(instructor4);
        this.add(instructor5);
        this.add(instructor6);
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
    public void delete(String id) {
        Instructor instructor = this.findByID(id);
        this.allInstructors.remove(instructor);
    }

    @Override
    public void update(String id, Instructor instructor) {
        Instructor instr = this.findByID(id);
        int position = this.allInstructors.indexOf(instr);
        this.allInstructors.set(position, instructor);
    }

    @Override
    public Instructor findByID(String id) {
        for (Instructor instructor: this.allInstructors){
            if(instructor.getID().equals(id))
                return instructor;
        }
        return null;
    }

}

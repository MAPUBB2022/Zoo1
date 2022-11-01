package repository.memoryRepo;

import domain.Instructor;
import repository.InstructorRepository;

import java.util.List;

public class InMemoryInstructorRepository implements InstructorRepository {
    @Override
    public void add(Instructor instructor) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Instructor instructor, Integer integer) {

    }

    @Override
    public Instructor findByID(Integer integer) {
        return null;
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return null;
    }
}

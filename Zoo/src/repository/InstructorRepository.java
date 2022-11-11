package repository;
import domain.Guest;
import domain.Instructor;

import java.util.List;

public interface InstructorRepository extends ICrudRepository<String, Instructor>{
    List<Instructor> getAllInstructors();
    void add(Instructor instructor);
    void delete(String id);
    void update(Instructor instructor, String id);
    Instructor findByID(String id);
}

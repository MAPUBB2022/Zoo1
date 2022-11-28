package repository;
import domain.Guest;
import domain.Instructor;

import java.util.List;

public interface InstructorRepository extends ICrudRepository<String, Instructor>{
    List<Instructor> getAllInstructors();
}

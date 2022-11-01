package repository;
import domain.Guest;
import domain.Instructor;

import java.util.List;

public interface InstructorRepository extends ICrudRepository<Integer, Instructor>{
    List<Instructor> getAllInstructors();
}

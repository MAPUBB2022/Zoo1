package mockito_database;

import domain.Attraction;
import domain.Instructor;
import domain.Weekday;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.JDBCrepository.JdbcAttractionRepository;
import repository.JDBCrepository.JdbcInstructorRepository;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JdbcInstructorRepositoryTest {
    @Mock
    JdbcInstructorRepository jdbcInstructorRepository;
    @Mock
    private EntityManager manager;
    @Mock
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        jdbcInstructorRepository = new JdbcInstructorRepository("default", "jdbc:postgresql://localhost:5432/Zoo", "postgres", "timi3678");
    }

    @Test
    public void testGetAllInstructors() {
        assertEquals(jdbcInstructorRepository.getAllInstructors().size(), 6);
    }

    @Test
    public void testAdd() {
        Instructor instructor1 = new Instructor("i7", "Anne", "Parker", "888888");
        List<Instructor> origin = jdbcInstructorRepository.getAllInstructors();
        int originsize = origin.size();
        jdbcInstructorRepository.add(instructor1);
        assertEquals(jdbcInstructorRepository.getAllInstructors().size(), originsize + 1);
    }

    @Test
    public void testDelete() {
        int originsize = jdbcInstructorRepository.getAllInstructors().size();
        jdbcInstructorRepository.delete("i1");
        assertEquals(jdbcInstructorRepository.getAllInstructors().size(), originsize - 1);
    }

    @Test
    public void testUpdate() {
        Instructor instructor = jdbcInstructorRepository.getAllInstructors().get(0);
        String id = instructor.getID();
        Instructor instructorNew = new Instructor("i100", "Anne", "Kitty", "qtserver");
        jdbcInstructorRepository.update(instructorNew, id);
        assertNotEquals(jdbcInstructorRepository.getAllInstructors().get(0), instructorNew);
    }

    @Test
    public void testFindById() {
        Instructor instructorNew = new Instructor("i55", "Kis", "Maria", "tmt99");
        jdbcInstructorRepository.add(instructorNew);
        assertEquals(jdbcInstructorRepository.findByID("i55"), instructorNew);
    }
}

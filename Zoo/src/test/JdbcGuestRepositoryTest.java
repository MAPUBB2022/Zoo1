package mockito_database;

import domain.Guest;
import domain.Instructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import repository.JDBCrepository.JdbcAttractionRepository;
import repository.JDBCrepository.JdbcGuestRepository;
import repository.JDBCrepository.JdbcInstructorRepository;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class JdbcGuestRepositoryTest {
    @Mock
    private JdbcGuestRepository jdbcGuestRepository;
    @Mock
    private JdbcAttractionRepository jdbcAttractionRepository;
    @Mock
    private JdbcInstructorRepository jdbcInstructorRepository;
    @Mock
    private EntityManager manager;
    @Mock
    private Connection connection;

    @Before
    public void setUp() throws Exception{
        jdbcInstructorRepository = new JdbcInstructorRepository("default","jdbc:postgresql://localhost:5432/Zoo","postgres","timi3678");
        jdbcAttractionRepository = new JdbcAttractionRepository(jdbcInstructorRepository);
        jdbcGuestRepository = new JdbcGuestRepository(jdbcAttractionRepository);
    }

    @Test
    public void testGetAllGuests() {
        assertEquals(jdbcGuestRepository.getAllGuests().size(), 18);
    }

    @Test
    public void testAdd() {
        Guest guest = new Guest("anna11", "Anna", "Nagy", "annqq", LocalDate.of(2002,3,1));
        List<Guest> origin = jdbcGuestRepository.getAllGuests();
        int originsize = origin.size();
        jdbcGuestRepository.add(guest);
        assertEquals(jdbcGuestRepository.getAllGuests().size(), originsize + 1);
    }

    @Test
    public void testDelete() {
        int originsize = jdbcGuestRepository.getAllGuests().size();
        jdbcInstructorRepository.delete(jdbcGuestRepository.getAllGuests().get(0).getID());
        assertEquals(jdbcInstructorRepository.getAllInstructors().size(), originsize - 1);
    }

    @Test
    public void testUpdate() {
        Guest guest = jdbcGuestRepository.getAllGuests().get(0);
        String id = guest.getID();
        Guest guestNew = new Guest("anna101", "Anna", "Kis", "annqq1", LocalDate.of(2008,3,1));
        jdbcGuestRepository.update(guestNew, id);
        assertNotEquals(jdbcInstructorRepository.getAllInstructors().get(0), guestNew);
    }

    @Test
    public void testFindById() {
        Guest guestNew = new Guest("corni11", "Cornelia", "Kun", "kkk99", LocalDate.of(2008,7,17));
        jdbcGuestRepository.add(guestNew);
        assertEquals(jdbcInstructorRepository.findByID("corni11"), guestNew);
    }
}

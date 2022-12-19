package mockito_database;

import domain.Attraction;
import domain.Instructor;
import domain.Weekday;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import repository.AttractionRepository;
import repository.InstructorRepository;
import repository.JDBCrepository.JdbcAttractionRepository;
import repository.JDBCrepository.JdbcInstructorRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JdbcAttractionRepositoryTest{
    @Mock
    private JdbcAttractionRepository jdbcAttractionRepository;
    @Mock
    private JdbcInstructorRepository instructorRepository;
    @Mock
    private EntityManager manager;
    @Mock
    private Connection connection;

    @Before
    public void setUp() throws Exception{
        instructorRepository = new JdbcInstructorRepository("default","jdbc:postgresql://localhost:5432/Zoo","postgres","timi3678");
        jdbcAttractionRepository = new JdbcAttractionRepository(instructorRepository);
    }

    @Test
    public void testGetAllAttractions(){
        assertEquals(jdbcAttractionRepository.getAllAttractions().size(),8);
    }

    @Test
    public void testAdd(){
        Attraction attraction1 = new Attraction("Lions", 500, instructorRepository.getAllInstructors().get(0),55.5, "A777", Weekday.MONDAY);
        List<Attraction> origin = jdbcAttractionRepository.getAllAttractions();
        jdbcAttractionRepository.add(attraction1);
        int originsize = origin.size();
        assertEquals(jdbcAttractionRepository.getAllAttractions().size(),originsize+1);
    }

    @Test
    public void testDelete(){
        jdbcAttractionRepository.delete("ZAMONDAY");
        assertEquals(jdbcAttractionRepository.getAllAttractions().size(), 8);
    }

    @Test
    public void testUpdate(){
        Attraction attraction = jdbcAttractionRepository.getAllAttractions().get(0);
        String id = attraction.getID();
        Attraction attractionNew = new Attraction("Muhaha", 100, instructorRepository.getAllInstructors().get(0),400, "sss", Weekday.WEDNESDAY);
        jdbcAttractionRepository.update(attractionNew,id);
        assertNotEquals(attraction,attractionNew);
    }

    @Test
    public void testFindById(){
        Attraction attractionNew = new Attraction("Kalambo", 111, instructorRepository.getAllInstructors().get(3),400, "89YD", Weekday.SATURDAY);
        jdbcAttractionRepository.add(attractionNew);
        assertEquals(jdbcAttractionRepository.findByID("K8SATURDAY"),attractionNew);
//
//        Throwable throwable = assertThrows(NoResultException.class, ()->jdbcAttractionRepository.findByID("8989898"));
//        assertEquals(throwable,null);
    }
}

package mockito_database;

import domain.Attraction;
import domain.Guest;
import domain.Instructor;
import domain.Weekday;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AttractionDataTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private Attraction attraction;

    @Before
    public void setUp() throws Exception {
        assertNotNull(dataSource);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(dataSource.getConnection()).thenReturn(connection);

        Instructor instructorNew = new Instructor("1234", "Ion", "Crisan", "5555");
        attraction = new Attraction("Animals1", 100, instructorNew,100.2,"A1234", Weekday.MONDAY);

        when(resultSet.first()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn(attraction.getID());
        when(resultSet.getInt(2)).thenReturn(attraction.getCapacity());
        when(resultSet.getObject(3)).thenReturn(attraction.getInstructor());
        when(resultSet.getDouble(4)).thenReturn(attraction.price);
        when(resultSet.getString(5)).thenReturn(attraction.location);
        when(resultSet.getString(6)).thenReturn(attraction.day.toString());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void createAttraction() {
        new AttractionData(dataSource).create(attraction);
    }

    @Test
    public void createAndRetrieveAttraction() throws Exception {
        AttractionData attractionData = new AttractionData(dataSource);
        attractionData.create(attraction);
        Attraction a = attractionData.retrieve("Animals1");
        assertEquals(attraction,a);
    }
}

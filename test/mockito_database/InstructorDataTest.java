package mockito_database;

import domain.Instructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstructorDataTest{

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private Instructor instructor;

    @Before
    public void setUp() throws Exception {
        assertNotNull(dataSource);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(dataSource.getConnection()).thenReturn(connection);

        instructor = new Instructor("555","Johannes", "Jonas", "1234");

        when(resultSet.first()).thenReturn(true);
        when(resultSet.getString(2)).thenReturn(instructor.getFirstName());
        when(resultSet.getString(3)).thenReturn(instructor.getLastName());
        when(resultSet.getString(4)).thenReturn(instructor.getPassword());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void createInstructor() {
        new InstructorData(dataSource).create(instructor);
    }

    @Test
    public void createAndRetrieveInstructor() throws Exception {
        InstructorData data = new InstructorData(dataSource);
        data.create(instructor);
        Instructor i = data.retrieve("555");
        assertEquals(instructor, i);
    }

}
package mockito_database;

import domain.Guest;
import domain.Instructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GuestDataTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private Guest guest;

    @Before
    public void setUp() throws Exception {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(dataSource.getConnection()).thenReturn(connection);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        String birthday = "16/08/2016";
        LocalDate localDate = LocalDate.parse(birthday, formatter); //convert String to LocalDate
        guest = new Guest("ana111", "Comsa", "Ana", "1234", localDate);

        when(resultSet.first()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn(guest.getID());
        when(resultSet.getString(2)).thenReturn(guest.getFirstName());
        when(resultSet.getString(3)).thenReturn(guest.getLastName());
        when(resultSet.getString(4)).thenReturn(guest.getPassword());
        when(resultSet.getDate(5)).thenReturn(Date.valueOf(guest.getBirthday()));
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void createGuest() {
        new GuestData(dataSource).create(guest);
    }

    @Test
    public void createAndRetrieveGuest() throws Exception {
        GuestData guestData = new GuestData(dataSource);
        guestData.create(guest);
        Guest g = guestData.retrieve("ana111");
        assertEquals(guest, g);
    }
}

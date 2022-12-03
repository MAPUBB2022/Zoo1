package mockito_database;

import domain.Attraction;
import domain.Guest;
import domain.Instructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GuestData {
    private DataSource dataSource;
    public  GuestData(DataSource ds){
        this.dataSource = ds;
    }

    public void create(Guest guest){
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO guest (username, firstName, lastName, password, birthday) VALUES (?,?,?)");
            preparedStatement.setString(1,guest.getID());
            preparedStatement.setString(2,guest.getFirstName());
            preparedStatement.setString(3,guest.getLastName());
            preparedStatement.setString(4,guest.getPassword());
            preparedStatement.setString(5, guest.getBirthday().toString());
        }catch (SQLException sqlException){
            try {
                throw sqlException.getCause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Guest retrieve(String ID){
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT username, firstName, lastName, password, birthday FROM guest WHERE username = ?");
            preparedStatement.setString(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.first()){
                return null;
            }
            Guest guest = new Guest();
            guest.setFirstName(resultSet.getString(2));
            guest.setLastName(resultSet.getString(3));
            guest.setPassword(resultSet.getString(4));
            guest.setBirthday(LocalDate.parse(resultSet.getString(5)));
            connection.close();
            return guest;
        }catch (SQLException sqlException){
            try {
                throw sqlException.getCause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void update(Guest guest){
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE guest SET firstName=?, lastName=?, password=?, birthday=? WHERE username =?");
            preparedStatement.setString(2,guest.getFirstName());
            preparedStatement.setString(3,guest.getLastName());
            preparedStatement.setString(4,guest.getPassword());
            preparedStatement.setString(5, String.valueOf(guest.getBirthday()));
            preparedStatement.executeUpdate();
            connection.close();
        }
        catch (SQLException sqlException){
            try {
                throw sqlException.getCause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void delete(String ID){
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM guest WHERE username=?");
            preparedStatement.setString(1, ID);
            preparedStatement.executeUpdate();
            connection.close();
        }
        catch (SQLException sqlException){
            try {
                throw sqlException.getCause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

    }
}

package mockito_database;

import domain.Attraction;
import domain.Instructor;

import javax.sql.DataSource;
import java.sql.*;

public class AttractionData {
    private DataSource dataSource;
    public  AttractionData(DataSource ds){
        this.dataSource = ds;
    }

    public void create(Attraction attraction){
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO attraction (ID,name,capacity,instructor,price,location,day) VALUES (?,?,?)");
            preparedStatement.setString(1,attraction.getID());
            preparedStatement.setString(2,attraction.name);
            preparedStatement.setInt(3,attraction.getCapacity());
            preparedStatement.setObject(4,attraction.getInstructor());
            preparedStatement.setDouble(5, attraction.price);
            preparedStatement.setString(6,attraction.location);
            preparedStatement.setString(7,attraction.day.toString());
            //? preparedStatement.set...(7,attraction.day);
        }catch (SQLException sqlException){
            try {
                throw sqlException.getCause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Attraction retrieve(String ID){
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, name, capacity,instructor,price,location,day FROM attraction WHERE ID = ?");
            preparedStatement.setString(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.first()){
                return null;
            }
            Attraction attraction = new Attraction();
            attraction.setInstructor((Instructor) resultSet.getObject(4));
            connection.close();
            return attraction;
        }catch (SQLException sqlException){
            try {
                throw sqlException.getCause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void update(Attraction attraction){
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE attraction SET instructor=?");
            preparedStatement.setObject(4,(Instructor)attraction.getInstructor());
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
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM attraction WHERE attraction_id=?");
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

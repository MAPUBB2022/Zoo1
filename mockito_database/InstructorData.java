package mockito_database;

import domain.Attraction;
import domain.Instructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.sql.*;

public class InstructorData {
    private DataSource dataSource;
    public  InstructorData(DataSource ds){
        this.dataSource = ds;
    }

    public void create(Instructor instructor){
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO instructor (instructor_id, firstname, lastname, password) VALUES (?,?,?,?)");
            preparedStatement.setString(1,instructor.getID());
            preparedStatement.setString(2,instructor.getFirstName());
            preparedStatement.setString(3,instructor.getLastName());
            preparedStatement.setString(4,instructor.getPassword());
        }catch (SQLException sqlException){
            try {
                throw sqlException.getCause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Instructor retrieve(String ID){
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT instructor_id, firstname, lastname, password FROM instructor WHERE ID = ?");
            preparedStatement.setString(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.first()){
                return null;
            }
            Instructor instructor = new Instructor();
            instructor.setFirstName(resultSet.getString(2));
            instructor.setLastName(resultSet.getString(3));
            instructor.setPassword(resultSet.getString(4));
            connection.close();
            return instructor;
        }catch (SQLException sqlException){
            try {
                throw sqlException.getCause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void update(Instructor instructor){
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE instructor SET firstname=?, lastname=?, password=?");
            preparedStatement.setString(2,instructor.getFirstName());
            preparedStatement.setString(3,instructor.getLastName());
            preparedStatement.setString(4,instructor.getPassword());
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
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM instructor WHERE instructor_id=?");
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


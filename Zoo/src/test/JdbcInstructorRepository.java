package repository.JDBCrepository;

import domain.Guest;
import domain.Instructor;
import repository.InstructorRepository;

import javax.persistence.*;
import java.sql.*;
import java.util.List;

public class JdbcInstructorRepository implements InstructorRepository {
    private EntityManager manager;
    private Connection connection;

    public JdbcInstructorRepository(String persistanceName, String url, String user, String password) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistanceName);
        this.manager =  factory.createEntityManager();
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e){
            System.out.println("");
        }

        // check if table exists already in database or not
//        try {
//            DatabaseMetaData databaseMetaData = connection.getMetaData();
//            ResultSet resultSet = databaseMetaData.getTables(null, null, "INSTRUCTOR",
//                    new String[] {"TABLE"});
//            if(resultSet.next()){    // table exists
//                manager.getTransaction().begin();
//                Query query = manager.createNativeQuery("DROP TABLE instructor", Guest.class);
//                query.getResultList();
//                manager.getTransaction().commit();
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        this.populateInstructors();
    }

    private void populateInstructors(){
        Instructor instructor1 = new Instructor("i1","James", "Parker", "123456");
        Instructor instructor2 = new Instructor("i2","James", "John", "qwerty");
        Instructor instructor3 = new Instructor("i3", "Lucy", "Misterious", "abc123");
        Instructor instructor4 = new Instructor("i4", "Katy", "Gal", "123321");
        Instructor instructor5 = new Instructor("i5","Camila", "Pop", "password1");
        Instructor instructor6 = new Instructor("i6","Mircea", "Miron", "abcd1234");

        manager.getTransaction().begin();
        manager.persist(instructor1); manager.persist(instructor2); manager.persist(instructor3);
        manager.persist(instructor4); manager.persist(instructor5); manager.persist(instructor6);
        manager.getTransaction().commit();
    }

    @Override
    public List<Instructor> getAllInstructors() {
        manager.getTransaction().begin();
        Query query = manager.createNativeQuery("select * from instructor", Instructor.class);
        List<Instructor> instructors = (List<Instructor>) query.getResultList();
        manager.getTransaction().commit();
        return instructors;
    }

    @Override
    public void add(Instructor instructor) {
//            try{
        if (this.findByID(instructor.getID())==null)
        {
            manager.getTransaction().begin();
            manager.persist(instructor);
            manager.getTransaction().commit();
        }
        else System.out.println("Es gibt schone eine Instruktor mit diese ID");
    }
    @Override
    public void delete(String id) {
        Instructor instructor = this.findByID(id);
        manager.getTransaction().begin();
        manager.remove(instructor);
        manager.getTransaction().commit();
    }

    @Override
    public void update(Instructor instructor, String idInstructor) {
        Instructor instr = this.findByID(idInstructor);
        if (instr!=null)
        {
            manager.getTransaction().begin();
            instr.setFirstName(instructor.getFirstName());
            instr.setLastName(instructor.getLastName());
            instr.setPassword(instructor.getPassword());
            instr.setFinalSum(instructor.getFinalSum());
            instr.setAttractions(instructor.getAttractions());
            manager.getTransaction().commit();
        }
    }

    @Override
    public Instructor findByID(String idInstructor) {
        try{
            manager.getTransaction().begin();
            Instructor instructor = manager.find(Instructor.class, idInstructor);
            manager.getTransaction().commit();
            return instructor;
        } catch (NoResultException e){
            return null;}
    }

    public EntityManager getManager() {
        return manager;
    }

    public Connection getConnection() {
        return connection;
    }
}
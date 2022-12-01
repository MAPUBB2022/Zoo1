package repository.filerepo;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.*;
import repository.InstructorRepository;
import domain.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JSONInstructorRepository implements InstructorRepository {

    private String filepath;

    public JSONInstructorRepository(String filepath) {
        this.filepath = filepath;
        this.populateInstructors();
    }

    public void populateInstructors(){

        Instructor instructor1 = new Instructor("i1","James", "Parker", "123456");
        Instructor instructor2 = new Instructor("i2","James", "John", "qwerty");
        Instructor instructor3 = new Instructor("i3", "Lucy", "Misterious", "abc123");
        Instructor instructor4 = new Instructor("i4", "Katy", "Gal", "123321");
        Instructor instructor5 = new Instructor("i5","Camila", "Pop", "password1");
        Instructor instructor6 = new Instructor("i6","Mircea", "Miron", "abcd1234");

        this.add(instructor1);
        this.add(instructor2);
        this.add(instructor3);
        this.add(instructor4);
        this.add(instructor5);
        this.add(instructor6);

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            for(Instructor i: this.getAllInstructors()) {
                mapper.writeValue(Paths.get(filepath).toFile(), i);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Instructor> getAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();

        try {    // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // put into a list the elements
            instructors = Arrays.asList(mapper.readValue(Paths.get(filepath).toFile(),Instructor[].class));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return instructors;
    }

    @Override
    public void delete(String id) {
        try {    // create object mapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // read the elements
            JsonNode jsonNode = objectMapper.readTree(new File(filepath));
            Iterator<JsonNode> nodes = jsonNode.elements();

            // until there is a new element
            while (nodes.hasNext()) {
                if (nodes.next().get("id").textValue().equals(id)) {
                    nodes.remove();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Instructor findByID(String id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // put the elements into a list
            Instructor[] instructors = mapper.readValue(new File(filepath), Instructor[].class);
            for (Instructor i: instructors) {
                if(i.getID().equals(id)){   // find the specific object
                    return i;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(Instructor instructor, String id) {
        try{
            // create object mapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.readerForUpdating(findByID(id)).readValue(instructor.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Instructor instructor) {
//        List<Instructor> instructorList = new ArrayList<>();
//        try{
//            instructorList = this.getAllInstructors();
//
//            // delete the entire file
//            File file = new File(filepath);
//            file.delete();
//
//            instructorList.add(instructor);
//
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(Paths.get(filepath).toFile(),instructorList);
//        } catch (StreamWriteException e) {
//            throw new RuntimeException(e);
//        } catch (DatabindException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}

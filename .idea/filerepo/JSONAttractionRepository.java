package repository.filerepo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import domain.*;
import net.bytebuddy.jar.asm.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import repository.AttractionRepository;
import repository.InstructorRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JSONAttractionRepository implements AttractionRepository{

    private String filepath;
    private final InstructorRepository instructorRepository;

    public JSONAttractionRepository(String filepath, InstructorRepository instructorRepository) {
        this.filepath = filepath;
        this.instructorRepository = instructorRepository;
        populateAttractions();
    }

    public void populateAttractions(){
        List<Instructor> instructors = this.instructorRepository.getAllInstructors();
        Instructor instructor1 = instructors.get(0);
        Instructor instructor2 = instructors.get(1);
        Instructor instructor3 = instructors.get(2);
        Instructor instructor4 = instructors.get(3);
        Instructor instructor5 = instructors.get(4);
        Instructor instructor6 = instructors.get(5);

        Attraction attraction1 = new Attraction("Zoo time",100, instructor1,180.99, "A456", Weekday.MONDAY);
        Attraction attraction2 = new Attraction("Elephantastic",200, instructor2,99.99,"B456", Weekday.TUESDAY);
        Attraction attraction3 = new Attraction("Fluffy Animals",10, instructor3,55.00,"A456", Weekday.WEDNESDAY);
        Attraction attraction4 = new Attraction("Angry Panda",17, instructor4,70.00,"X588", Weekday.THURSDAY);
        Attraction attraction5 = new Attraction("White Lion",8, instructor5,250.00,"F444", Weekday.FRIDAY);
        Attraction attraction6 = new Attraction("White Lion",80, instructor2,170.89,"T545", Weekday.SATURDAY);
        Attraction attraction7 = new Attraction("White Lion",55, instructor4,60.50,"RQ67", Weekday.SUNDAY);
        Attraction attraction8 = new Attraction("VIP zoo show",10, instructor6,300.87,"BRT60", Weekday.WEDNESDAY);

        instructor1.addAttraction(attraction1);
        instructor2.addAttraction(attraction2);
        instructor3.addAttraction(attraction3);
        instructor4.addAttraction(attraction4);
        instructor5.addAttraction(attraction5);
        instructor2.addAttraction(attraction6);
        instructor4.addAttraction(attraction7);
        instructor6.addAttraction(attraction8);

        List<Attraction> attractions = new ArrayList<>();

        attractions.add(attraction1);
        attractions.add(attraction2);
        attractions.add(attraction3);
        attractions.add(attraction4);
        attractions.add(attraction5);
        attractions.add(attraction6);
        attractions.add(attraction7);
        attractions.add(attraction8);

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            for(Attraction attraction: attractions)
            {
                mapper.writeValue(Paths.get(filepath).toFile(), attraction);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<Attraction> getAllAttractions(){
        List<Attraction> attractions = new ArrayList<>();

        try {    // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // put into a list the elements
            attractions = Arrays.asList(mapper.readValue(Paths.get(filepath).toFile(),Attraction[].class));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return attractions;
    }

    @Override
    public void add(Attraction attraction) { // ??
        JSONArray list = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("name", attraction.name);
        object.put("capacity", attraction.getCapacity());
        object.put("price", attraction.price);
        object.put("location", attraction.location);
        object.put("weekday", attraction.day);
        list.add(object);
    }

    @Override
    public void delete(String ID){
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // read the elements
            JsonNode jsonNode = objectMapper.readTree(new File(filepath));
            Iterator<JsonNode> nodes = jsonNode.elements();

            // until there is a new element
            while (nodes.hasNext()) {
                if (nodes.next().get("id").textValue().equals(ID)) {
                    nodes.remove();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Attraction attraction, String ID) {
        try{
            // create object mapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.readerForUpdating(findByID(ID)).readValue(attraction.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Attraction findByID(String id) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // put the elements into a list
            List<Attraction> attractions = Arrays.asList( objectMapper.readValue(new File(filepath), Attraction[].class) );
            for (Attraction attraction: attractions) {
                if(attraction.getID().equals(id)){   // find the specific object
                    return attraction;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

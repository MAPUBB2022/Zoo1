package repository.filerepo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import domain.*;
import org.json.simple.JSONObject;
import repository.AttractionRepository;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JSONAttractionRepository implements AttractionRepository{

    private String filepath;

    public JSONAttractionRepository(String filepath) {
        this.filepath = filepath;
        populateAttractions();
    }

    private void populateAttractions(){
        List<Attraction> a1 = null;
        List<Attraction> a2 = null;
        List<Attraction> a3 = null;
        List<Attraction> a4 = null;
        List<Attraction> a5 = null;
        Instructor instructor1 = new Instructor(a1,"James", "Parker");
        Instructor instructor2 = new Instructor(a2, "James", "John");
        Instructor instructor3 = new Instructor(a3, "Misterious", "Lucy");
        Instructor instructor4 = new Instructor(a4, "Gal", "Katy");
        Instructor instructor5 = new Instructor(a5, "Pop", "Camila");
        List<Guest> g1 = null;
        List<Guest> g2 = null;
        List<Guest> g3 = null;
        List<Guest> g4 = null;
        List<Guest> g5 = null;
        List<Guest> g6 = null;
        List<Guest> g7 = null;
        Attraction attraction1 = new Attraction("Zoo time",100,instructor1, g1, 180.99, "A456", Weekday.MONDAY);
        Attraction attraction2 = new Attraction("Elephantastic",200,instructor2,g2,99.99,"B456",Weekday.TUESDAY);
        Attraction attraction3 = new Attraction("Fluffy Animals",10,instructor3,g3,55.00,"A456",Weekday.WEDNESDAY);
        Attraction attraction4 = new Attraction("Angry Panda",17,instructor4,g4,70.00,"X588",Weekday.THURSDAY);
        Attraction attraction5 = new Attraction("White Lion",8,instructor5,g5,250.00,"F444",Weekday.FRIDAY);
        Attraction attraction6 = new Attraction("White Lion",80,instructor2,g6,170.89,"T545",Weekday.SATURDAY);
        Attraction attraction7 = new Attraction("White Lion",55,instructor4,g7,60.50,"RQ67",Weekday.SUNDAY);
        this.add(attraction1);
        this.add(attraction2);
        this.add(attraction3);
        this.add(attraction4);
        this.add(attraction5);
        this.add(attraction6);
        this.add(attraction7);
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            for(Attraction a: this.getAllAttractions()) {
                mapper.writeValue(Paths.get(filepath).toFile(), a);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<Attraction> getAllAttractions() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Attraction> attractions = Arrays.asList(mapper.readValue(Paths.get(filepath).toFile(),Attraction[].class));
        return attractions;
    }

    @Override
    public void add(Attraction attraction) {
        JSONObject object = new JSONObject();
        object.put("name", attraction.name);
        object.put("capacity", attraction.getCapacity());
        object.put("price", attraction.price);
        object.put("location", attraction.location);
        // object.put("weekday", attraction.day);  ?

    }

    @Override
    public void delete(String ID) throws IOException {
        for(Attraction a: this.getAllAttractions()) {
            if(a.getID() == ID) {
                int idx = getAllAttractions().indexOf(a);
                this.getAllAttractions().remove(idx);
                // wir konnen auch ohne index, nur mit obj machen
            }
        }
    }

    @Override
    public void update(Attraction attraction, String ID) throws IOException {
        Attraction attr = this.findByID(ID);
        int position = this.getAllAttractions().indexOf(attr);
        this.getAllAttractions().set(position,attraction);
    }

    @Override
    public Attraction findByID(String id) throws IOException {
        for (Attraction attraction: this.getAllAttractions()){
            if (attraction.getID().equals(id))
                return attraction;
        }
        return null;
    }
}

package repository.filerepo;


import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.*;
import domain.*;
import org.json.simple.JSONObject;
import repository.AttractionRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JSONAttractionRepository implements AttractionRepository{

    private String filepath;

    public JSONAttractionRepository(String filepath) {
        this.filepath = filepath;
        this.populateAttractions();
    }

    public void populateAttractions(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        Guest g1 = new Guest("popamaria08", "Popa", "Maria", "789DR",
                LocalDate.parse("16/08/2002", formatter));
        Guest g2 = new Guest("popaana09", "Popa", "Ana", "XD45",
                LocalDate.parse("16/08/1999", formatter));
        Guest g3 = new Guest("ionandrei10", "Ion", "Andrei", "DSX44",
                LocalDate.parse("01/01/1968", formatter));
        Guest g4 = new Guest("tamasraul77", "Tamas", "Raul", "YU89KI",
                LocalDate.parse("01/01/1966", formatter));
        Guest g5 = new Guest("tamasraul78", "Tamas", "Raul", "OYH789",
                LocalDate.parse("01/03/2001", formatter));
        Guest g6 = new Guest("crisanioana45", "Crisan", "Ioana", "G6789",
                LocalDate.parse("01/03/2010", formatter));
        Guest g7 = new Guest("crisanioana444", "Crisan", "Ioana", "56789",
                LocalDate.parse("01/09/2015", formatter));
        Guest g8 = new Guest("tamasilinca30", "Tamas", "Ilinca", "TS555",
                LocalDate.parse("09/09/1955", formatter));
        Guest g9 = new Guest("dragosenigel9", "Dragos", "Enigel", "89766",
                LocalDate.parse("11/11/2013", formatter));
        Guest g10 = new Guest("michaeljackson88", "Michael", "Jackson", "MK345567",
                LocalDate.parse("29/08/1958", formatter));
        Instructor instructor1 = new Instructor("JP1","James", "Parker", "12345");
        Instructor instructor2 = new Instructor("JJ2", "James", "John", "6789");
        Instructor instructor3 = new Instructor("ML", "Misterious", "Lucy", "X456");
        Instructor instructor4 = new Instructor("GK4", "Gal", "Katy", "67H85");
        Instructor instructor5 = new Instructor("PC5", "Pop", "Camila", "I890");
        Instructor instructor6 = new Instructor("XD7", "Comsa", "Andrei", "77781");

        List<Guest> guestList1 = new ArrayList<Guest>();
        guestList1.add(g1);
        guestList1.add(g5);
        guestList1.add(g6);
        guestList1.add(g8);
        List<Guest> guestList2 = new ArrayList<Guest>();
        guestList2.add(g1);
        guestList2.add(g2);
        guestList2.add(g3);
        guestList2.add(g4);
        List<Guest> guestList3 = new ArrayList<Guest>();
        guestList3.add(g10);
        guestList3.add(g9);
        List<Guest> guestList4 = new ArrayList<Guest>();
        guestList4.add(g5);
        guestList4.add(g6);
        guestList4.add(g7);
        List<Guest> guestList5 = new ArrayList<Guest>();
        guestList5.add(g1);
        guestList5.add(g10);
        guestList5.add(g6);
        List<Guest> guestList6 = new ArrayList<Guest>();
        guestList6.add(g1);
        guestList6.add(g2);
        guestList6.add(g3);
        guestList6.add(g4);
        guestList6.add(g5);
        guestList6.add(g6);
        guestList6.add(g7);
        List<Guest> guestList7 = new ArrayList<Guest>();
        guestList7.add(g4);
        guestList7.add(g5);
        guestList7.add(g6);
        guestList7.add(g7);
        guestList7.add(g8);
        guestList7.add(g9);
        guestList7.add(g10);
        Attraction attraction1 = new Attraction("Zoo time",7,instructor1, guestList7, 180.99, "A456", Weekday.MONDAY);
        Attraction attraction2 = new Attraction("Elephantastic",5,instructor2,guestList1,99.99,"B456",Weekday.TUESDAY);
        Attraction attraction3 = new Attraction("Fluffy Animals",4,instructor3,guestList2,55.00,"A456",Weekday.WEDNESDAY);
        Attraction attraction4 = new Attraction("Angry Panda",10,instructor4,guestList3,70.00,"X588",Weekday.THURSDAY);
        Attraction attraction5 = new Attraction("White Lion",8,instructor5,guestList4,250.00,"F444",Weekday.FRIDAY);
        Attraction attraction6 = new Attraction("White Lion",3,instructor2,guestList5,170.89,"T545",Weekday.SATURDAY);
        Attraction attraction7 = new Attraction("White Lion",11,instructor4,guestList6,60.50,"RQ67",Weekday.SUNDAY);
        Attraction attraction8 = new Attraction("VIP zoo show",10, instructor6,300.87,"BRT60", Weekday.WEDNESDAY);

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
    public List<Attraction> getAllAttractions() {
        List<Attraction> attractions = new ArrayList<>();

        try {    // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            attractions = Arrays.asList(mapper.readValue(Paths.get(filepath).toFile(),Attraction[].class));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return attractions;
    }

    @Override
    public void add(Attraction attraction) {
        List<Attraction> attractionList = new ArrayList<>();
        try{
            attractionList = this.getAllAttractions();

            // delete the entire file
            File file = new File(filepath);
            file.delete();

            attractionList.add(attraction);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(Paths.get(filepath).toFile(),attractionList);
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String ID){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File(filepath));
            Iterator<JsonNode> nodes = jsonNode.elements();
            while (nodes.hasNext()) {
                if (nodes.next().get("ID").textValue().equals(ID)) {
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
            String inputID = "{\"ID\": " + ID + " }";
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.readerForUpdating(attraction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Attraction findByID(String id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Attraction> attractions = Arrays.asList( mapper.readValue(new File(filepath), Attraction[].class) );

            for (Attraction a: attractions) {
                if(a.getID().equals(id)){
                    return a;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

package repository.filerepo;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.*;
import domain.*;

import repository.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JSONGuestRepository implements GuestRepository {

    private String filepath;

    public JSONGuestRepository(String filepath) {
        this.filepath = filepath;
        this.populateGuests();
    }

    public void populateGuests(){

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
        this.add(g1);
        this.add(g2);
        this.add(g3);
        this.add(g4);
        this.add(g5);
        this.add(g6);
        this.add(g7);
        this.add(g8);
        this.add(g9);
        this.add(g10);

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

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            for(Guest g: this.getAllGuests()) {
                mapper.writeValue(Paths.get(filepath).toFile(), g);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Guest> getAllGuests(){

        List<Guest> guests = new ArrayList<>();

        try {    // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            guests = Arrays.asList(mapper.readValue(Paths.get(filepath).toFile(),Guest[].class));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return guests;
    }

    @Override
    public void delete(String username) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File(filepath));
            Iterator<JsonNode> nodes = jsonNode.elements();
            while (nodes.hasNext()) {
                if (nodes.next().get("username").textValue().equals(username)) {
                    nodes.remove();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public Guest findByID(String id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Guest> guests = Arrays.asList( mapper.readValue(new File(filepath), Guest[].class) );

            for (Guest g: guests) {
                if(g.getID().equals(id)){
                    return g;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(Guest guest, String id) {
        try{
            String inputID = "{\"username\": " + id + " }";
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.readerForUpdating(guest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Guest guest) {
        List<Guest> guestList = new ArrayList<>();
        try{
            guestList = this.getAllGuests();

            // delete the entire file
            File file = new File(filepath);
            file.delete();

            guestList.add(guest);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(Paths.get(filepath).toFile(),guestList);
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

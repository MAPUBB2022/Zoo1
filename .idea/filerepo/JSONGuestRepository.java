package repository.filerepo;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.*;
import domain.*;

import repository.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JSONGuestRepository implements GuestRepository {

    private String filepath;
    private final AttractionRepository attractionRepository;

    public JSONGuestRepository(String filepath, AttractionRepository attractionRepository) {
        this.filepath = filepath;
        this.attractionRepository = attractionRepository;
        this.populateGuests();
    }

    public void populateGuests(){
        List<Attraction> attractions = attractionRepository.getAllAttractions();

        Attraction attraction1 = attractions.get(0);
        Attraction attraction2 = attractions.get(1);
        Attraction attraction3 = attractions.get(2);
        Attraction attraction4 = attractions.get(3);
        Attraction attraction5 = attractions.get(4);
        Attraction attraction6 = attractions.get(5);
        Attraction attraction7 = attractions.get(6);
        Attraction attraction8 = attractions.get(7);

        Guest guest1 = new Guest("maria01", "Maria", "Kis", "KM01", LocalDate.of(2002,2,1));
        Guest guest2 = new Guest("ioana.petru","Ioana", "Petru",  "1b38TC1li", LocalDate.of(1997,2,1));
        Guest guest3 = new Guest("comsa_ana","Ana", "Comsa", "abcd123", LocalDate.of(1967,3,1));
        Guest guest4 = new Guest("pop.oti","Otilia", "Pop", "animals", LocalDate.of(2002,4,11));
        Guest guest5 = new Guest("ion123","Ion", "Ionut", "zoo", LocalDate.of(1989,8,12));
        Guest guest6 = new Guest("timi11","Timea", "Gal", "gtig", LocalDate.of(2009,2,1));
        Guest guest7 = new Guest("g.emese","Emese", "Gal", "bcn10", LocalDate.of(2010,7,16));
        Guest guest8 = new Guest("ecaterinaa","Ecaterina", "Popa", "password123", LocalDate.of(2021,11,18));
        Guest guest9 = new Guest("katy99","Katy", "Perry", "1234", LocalDate.of(2018,3,3));
        Guest guest10 = new Guest("gomez.s","Selena", "Gomez", "selenaa", LocalDate.of(2001,5,28));
        Guest guest11 = new Guest("bieber_justin","Justin", "Bieber", "success", LocalDate.of(2000,3,23));
        Guest guest12 = new Guest("celined","Céline", "Dion", "titanic", LocalDate.of(1970,12,24));
        Guest guest13 = new Guest("leo_dicaprio","Leonardo", "DiCaprio", "titanic", LocalDate.of(1956,8,8));
        Guest guest14 = new Guest("roberts.julia","Julia", "Roberts", "sunshine", LocalDate.of(1999,7,7));
        Guest guest15 = new Guest("tom_hanks","Tom", "Hanks", "summer", LocalDate.of(1999,11,12));
        Guest guest16 = new Guest("gibson_mel","Mel", "Gibson", "abc123", LocalDate.of(1988,2,1));
        Guest guest17 = new Guest("jackie23","Jackie", "Chan", "passw0rd", LocalDate.of(1995,3,16));
        Guest guest18 = new Guest("terence_hill","Terence", "Hill", "111111", LocalDate.of(1988,9,29));

        guest1.addAttraction(attraction8); guest3.addAttraction(attraction8); guest5.addAttraction(attraction8); guest7.addAttraction(attraction8); guest9.addAttraction(attraction8);
        guest11.addAttraction(attraction8); guest13.addAttraction(attraction8); guest15.addAttraction(attraction8); guest17.addAttraction(attraction8); guest18.addAttraction(attraction8);

        attraction8.addGuest(guest1); attraction8.addGuest(guest3); attraction8.addGuest(guest5); attraction8.addGuest(guest7); attraction8.addGuest(guest9);
        attraction8.addGuest(guest11); attraction8.addGuest(guest13); attraction8.addGuest(guest15); attraction8.addGuest(guest17); attraction8.addGuest(guest18);

        guest1.addAttraction(attraction5); guest2.addAttraction(attraction5); guest3.addAttraction(attraction5); guest4.addAttraction(attraction5);

        attraction5.addGuest(guest1); attraction5.addGuest(guest2); attraction5.addGuest(guest3); attraction5.addGuest(guest4);

        List<Guest> guests = Arrays.asList(guest1,guest2, guest3, guest4, guest5, guest6, guest7, guest8, guest9,
                guest10, guest11, guest12, guest13, guest14, guest15, guest16, guest17, guest18);

        this.add(guest1); this.add(guest2); this.add(guest3); this.add(guest4); this.add(guest5);
        this.add(guest6); this.add(guest7); this.add(guest8); this.add(guest9); this.add(guest10);
        this.add(guest11); this.add(guest12); this.add(guest13); this.add(guest14); this.add(guest15);
        this.add(guest16); this.add(guest17); this.add(guest18);

        attraction5.getInstructor().calculateSum();
        attraction8.getInstructor().calculateSum();

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            for(Guest guest: guests) {
                mapper.writeValue(Paths.get(filepath).toFile(), guest);
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
            // create object mapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.readerForUpdating(findByID(id)).readValue(guest.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Guest guest) { // ?
//        List<Guest> guestList = new ArrayList<>();
//        try{
//            guestList = this.getAllGuests();
//
//            // delete the entire file
//            File file = new File(filepath);
//            file.delete();
//
//            guestList.add(guest);
//
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(Paths.get(filepath).toFile(),guestList);
//        } catch (StreamWriteException e) {
//            throw new RuntimeException(e);
//        } catch (DatabindException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
package repository.filerepo;
import domain.*;

import repository.*;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FileGuestRepository implements GuestRepository {

    private String filepath;
    private List<Guest> allGuests;
    private final AttractionRepository attractionRepository;

    public FileGuestRepository(String filepath, AttractionRepository attractionRepository) {
        this.filepath = filepath;
        this.attractionRepository = attractionRepository;
        this.allGuests = new ArrayList<>();
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

        this.add(guest1); this.add(guest2); this.add(guest3); this.add(guest4); this.add(guest5);
        this.add(guest6); this.add(guest7); this.add(guest8); this.add(guest9); this.add(guest10);
        this.add(guest11); this.add(guest12); this.add(guest13); this.add(guest14); this.add(guest15);
        this.add(guest16); this.add(guest17); this.add(guest18);

        attraction5.getInstructor().calculateSum();
        attraction8.getInstructor().calculateSum();

        File file = new File(this.filepath);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.allGuests);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Guest> getAllGuests(){
        List<Guest> guests  = new ArrayList<>();
        File file = new File(this.filepath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        boolean x = true;
        while(x) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
                Guest guest = (Guest) objectInputStream.readObject();
                if (guest != null) {
                    guests.add(guest);
                } else {
                    x = false;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return guests;
    }

    @Override
    public void delete(String username) {
        File file = new File(this.filepath);
        List<Guest> guests = new ArrayList<>();
        guests = getAllGuests();
        int idx;
        try {
            for(Guest guest: this.getAllGuests()){
                if (guest.getID().equals(username)){
                    idx = this.getAllGuests().indexOf(guest);
                    guests.remove(idx);
                }
            }
            file.delete();   // file loeschen

            // die Daten ohne geloschtes Element zuruckschreiben
            File file2 = new File(this.filepath);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(guests);
            objectOutputStream.close();
            fileOutputStream.close();
            this.allGuests = guests;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public Guest findByID(String id) {
        try {
            for (Guest guest : this.allGuests) {
                if (guest.getID().equals(id)) {
                    return guest;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(Guest guest, String id) {
        boolean found = false;
        File file = new File(this.filepath);
        try{
            //String inputID = "{\"ID\": " + ID + " }";
            Guest guest1 = this.findByID(id);
            int position = this.allGuests.indexOf(guest1);
            this.allGuests.set(position,guest);

            file.delete();   // file loeschen

            File file2 = new File(this.filepath);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.allGuests);
            objectOutputStream.close();
            fileOutputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Guest guest) {
        File file =  new File(this.filepath);
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(guest));
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



// JSON

//package repository.filerepo;
//import com.fasterxml.jackson.core.exc.StreamWriteException;
//import com.fasterxml.jackson.databind.*;
//import domain.*;
//
//import repository.*;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Paths;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//
//public class JSONGuestRepository implements GuestRepository {
//
//    private String filepath;
//
//    public JSONGuestRepository(String filepath) {
//        this.filepath = filepath;
//        this.populateGuests();
//    }
//
//    public void populateGuests(){
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
//        Guest g1 = new Guest("popamaria08", "Popa", "Maria", "789DR",
//                LocalDate.parse("16/08/2002", formatter));
//        Guest g2 = new Guest("popaana09", "Popa", "Ana", "XD45",
//                LocalDate.parse("16/08/1999", formatter));
//        Guest g3 = new Guest("ionandrei10", "Ion", "Andrei", "DSX44",
//                LocalDate.parse("01/01/1968", formatter));
//        Guest g4 = new Guest("tamasraul77", "Tamas", "Raul", "YU89KI",
//                LocalDate.parse("01/01/1966", formatter));
//        Guest g5 = new Guest("tamasraul78", "Tamas", "Raul", "OYH789",
//                LocalDate.parse("01/03/2001", formatter));
//        Guest g6 = new Guest("crisanioana45", "Crisan", "Ioana", "G6789",
//                LocalDate.parse("01/03/2010", formatter));
//        Guest g7 = new Guest("crisanioana444", "Crisan", "Ioana", "56789",
//                LocalDate.parse("01/09/2015", formatter));
//        Guest g8 = new Guest("tamasilinca30", "Tamas", "Ilinca", "TS555",
//                LocalDate.parse("09/09/1955", formatter));
//        Guest g9 = new Guest("dragosenigel9", "Dragos", "Enigel", "89766",
//                LocalDate.parse("11/11/2013", formatter));
//        Guest g10 = new Guest("michaeljackson88", "Michael", "Jackson", "MK345567",
//                LocalDate.parse("29/08/1958", formatter));
//        this.add(g1);
//        this.add(g2);
//        this.add(g3);
//        this.add(g4);
//        this.add(g5);
//        this.add(g6);
//        this.add(g7);
//        this.add(g8);
//        this.add(g9);
//        this.add(g10);
//
//        List<Guest> guestList1 = new ArrayList<Guest>();
//        guestList1.add(g1);
//        guestList1.add(g5);
//        guestList1.add(g6);
//        guestList1.add(g8);
//        List<Guest> guestList2 = new ArrayList<Guest>();
//        guestList2.add(g1);
//        guestList2.add(g2);
//        guestList2.add(g3);
//        guestList2.add(g4);
//        List<Guest> guestList3 = new ArrayList<Guest>();
//        guestList3.add(g10);
//        guestList3.add(g9);
//        List<Guest> guestList4 = new ArrayList<Guest>();
//        guestList4.add(g5);
//        guestList4.add(g6);
//        guestList4.add(g7);
//        List<Guest> guestList5 = new ArrayList<Guest>();
//        guestList5.add(g1);
//        guestList5.add(g10);
//        guestList5.add(g6);
//        List<Guest> guestList6 = new ArrayList<Guest>();
//        guestList6.add(g1);
//        guestList6.add(g2);
//        guestList6.add(g3);
//        guestList6.add(g4);
//        guestList6.add(g5);
//        guestList6.add(g6);
//        guestList6.add(g7);
//        List<Guest> guestList7 = new ArrayList<Guest>();
//        guestList7.add(g4);
//        guestList7.add(g5);
//        guestList7.add(g6);
//        guestList7.add(g7);
//        guestList7.add(g8);
//        guestList7.add(g9);
//        guestList7.add(g10);
//
//        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
//        try {
//            for(Guest g: this.getAllGuests()) {
//                mapper.writeValue(Paths.get(filepath).toFile(), g);
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Override
//    public List<Guest> getAllGuests(){
//
//        List<Guest> guests = new ArrayList<>();
//
//        try {    // create object mapper instance
//            ObjectMapper mapper = new ObjectMapper();
//            guests = Arrays.asList(mapper.readValue(Paths.get(filepath).toFile(),Guest[].class));
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return guests;
//    }
//
//    @Override
//    public void delete(String username) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(new File(filepath));
//            Iterator<JsonNode> nodes = jsonNode.elements();
//            while (nodes.hasNext()) {
//                if (nodes.next().get("username").textValue().equals(username)) {
//                    nodes.remove();
//                }
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//    @Override
//    public Guest findByID(String id) {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            List<Guest> guests = Arrays.asList( mapper.readValue(new File(filepath), Guest[].class) );
//
//            for (Guest g: guests) {
//                if(g.getID().equals(id)){
//                    return g;
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }
//
//    @Override
//    public void update(Guest guest, String id) {
//        try{
//            String inputID = "{\"username\": " + id + " }";
//            ObjectMapper objectMapper = new ObjectMapper();
//            ObjectReader objectReader = objectMapper.readerForUpdating(guest);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void add(Guest guest) {
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
//    }
//}

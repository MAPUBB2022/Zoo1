package registration;

import domain.Attraction;
import domain.Guest;
import domain.Instructor;
import domain.Weekday;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.AttractionRepository;
import repository.GuestRepository;
import repository.InstructorRepository;
import repository.memoryRepo.InMemoryAttractionRepository;
import repository.memoryRepo.InMemoryGuestRepository;
import repository.memoryRepo.InMemoryInstructorRepository;
import utils.NoMoreAvailableTicketsException;
import utils.NoSuchDataException;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static domain.Weekday.*;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationSystemTest{
    private InstructorRepository instructorRepository;
    private AttractionRepository attractionRepository;
    private GuestRepository guestRepository;
    private RegistrationSystem controller;

    @BeforeEach
    void setUp(){
        this.instructorRepository = new InMemoryInstructorRepository();
        this.attractionRepository = new InMemoryAttractionRepository(instructorRepository);
        try {
            this.guestRepository = new InMemoryGuestRepository(attractionRepository);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.controller = new RegistrationSystem(attractionRepository, guestRepository, instructorRepository);
    }

    @Test
    void testGetAllAttractions(){
        List<Attraction> attractions = null;
        try {
            attractions = controller.getAllAttractions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(attractions.size(), 8);
        assertEquals(attractions.get(0).name, "Zoo time");
        assertEquals(attractions.get(7).name, "VIP zoo show");
    }

    @Test
    void testAddAttraction(){
        // test with existing instructor id
        Attraction attraction1 = new Attraction("Sea lion show", 50, null, 120.4, "Pool1", SUNDAY);
        assertTrue(controller.addAttraction(attraction1, "i2"));
        // test with non-existent instructor id
        Attraction attraction2 = new Attraction("Orca show", 50, null, 100.10, "Pool1", MONDAY);
        assertFalse(controller.addAttraction(attraction2, "i15"));
        // add attraction after add new instructor
        Instructor instructor = new Instructor("i10","Samantha", "Thompson", "passwordABC");
        controller.addInstructor(instructor);
        assertTrue(controller.addAttraction(attraction2, "i10"));
    }

    @Test
    void testGetAllAttractionsWithFreePlaces(){
        try {
            assertEquals(controller.getAllAttractionsWithFreePlaces().size(), 7);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAttractionsSortedByTitle(){
        List<Attraction> sortedAttractions = null;
        try {
            sortedAttractions = controller.getAttractionsSortedByTitle();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(sortedAttractions.get(0).name, "Angry Panda");
        assertEquals(sortedAttractions.get(7).name, "Zoo time");
    }

    @Test
    void testGetAttractionsAfterAGivenDay(){
        try {
            assertEquals(controller.getAttractionsAfterAGivenDay(THURSDAY).size(), 4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Attraction> attractions = null;
        try {
            attractions = controller.getAttractionsAfterAGivenDay(SUNDAY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(attractions.size(), 1);
        try {
            assertEquals(attractions.get(0), controller.getAllAttractions().get(6));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetGuestsOfAttraction(){
        String idAttraction1 = null;
        try {
            idAttraction1 = controller.getAttractionsSortedByTitle().get(3).getID();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Guest> guests = null;
        try {
            guests = controller.getGuestsOfAttraction(idAttraction1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(guests.size(), 10);
        Guest guest = controller.findGuestByUsername("leo_dicaprio");
        assertTrue(guests.contains(guest));
        guest = controller.findGuestByUsername("gibson_mel");
        assertFalse(guests.contains(guest));

        // no guests registered for the attraction (empty list)
        String idAttraction2 = null;
        try {
            idAttraction2 = controller.getAttractionsSortedByTitle().get(0).getID();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            assertEquals(controller.getGuestsOfAttraction(idAttraction2), new ArrayList<Guest>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            assertFalse(controller.getGuestsOfAttraction(idAttraction2).contains(guest));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddGuest(){
        // add a new guest with existing username -> not possible
        Guest guest1 = new Guest("ioana.petru","Ioana", "Petru",  "123abc", LocalDate.of(1978,2,2));
        assertFalse(controller.addGuest(guest1));
        // add a new guest with unique username
        Guest guest2 = new Guest("ioana.petru2","Ioana", "Petru",  "123abc", LocalDate.of(1978,2,2));
        assertTrue(controller.addGuest(guest2));
        // add a new guest with same attributes as existing one (apart from username)
        Guest guest3 = new Guest("pop.otilia","Otilia", "Pop", "animals", LocalDate.of(2002,4,11));
        assertTrue(controller.addGuest(guest3));
        // add a guest twice -> not possible
        assertFalse(controller.addGuest(guest3));
    }

    @Test
    void testAddInstructor(){
        // add instructor with unique id
        Instructor instructor1 = new Instructor("i10","Samantha", "Thompson", "passwordABC");
        assertTrue(controller.addInstructor(instructor1));
        // add instructor twice -> not possible
        assertFalse(controller.addInstructor(instructor1));
        // add instructor with existing id -> not possible
        Instructor instructor2 = new Instructor("i4","John", "Simpson", "1234");
        assertFalse(controller.addInstructor(instructor2));
    }

    @Test
    void testGetAllGuests(){
        List<Guest> guests = this.controller.getAllGuests();
        assertEquals(guests.size(), 18);
        assertEquals(guests.get(0).getID(), "maria01");
        assertEquals(guests.get(17).getID(), "terence_hill");
    }

    @Test
    void testGetAttractionsOfGuest(){
        List<Attraction> guestAttractions = controller.getAttractionsOfGuest("maria01");
        assertEquals(guestAttractions.size(), 2);

        List<Attraction> attractions = null;
        try {
            attractions = controller.getAllAttractions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(guestAttractions.contains(attractions.get(7)));
        assertTrue(guestAttractions.contains(attractions.get(4)));
        assertFalse(guestAttractions.contains(attractions.get(5)));

        // guest who is not registered to any attractions
        guestAttractions = controller.getAttractionsOfGuest("celined");
        assertEquals(guestAttractions.size(), 0);
        assertEquals(guestAttractions, new ArrayList<Attraction>());
    }

    @Test
    void testGetFinalSumOfGuest()throws NoMoreAvailableTicketsException{
        // under 18 -> ticket with discount
        assertEquals(this.controller.getFinalSumOfGuest("katy99"),150.435);
        // above 60 -> ticket with discount
        assertEquals(this.controller.getFinalSumOfGuest("leo_dicaprio"),240.696, 0.1);

        assertEquals(this.controller.getFinalSumOfGuest("maria01"),550.87);
        String idAttraction = null;
        try {
            idAttraction = this.controller.getAllAttractions().get(0).getID();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // sign up for attraction -> sum increases
        assertTrue(this.controller.signUpForAttraction("maria01", idAttraction));
        assertEquals(this.controller.getFinalSumOfGuest("maria01"),731.86);
        // attraction deleted -> sum increases
        try {
            this.controller.deleteAttraction("i1",idAttraction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(this.controller.getFinalSumOfGuest("maria01"),550.87);
    }

    @Test
    void testGetSumFromGuests(){
        assertEquals(this.controller.getSumFromGuests("i2"),0);
        assertEquals(this.controller.getSumFromGuests("i6"),3008.7);
    }

    @Test
    void testGetGuestsSortedDescendingBySum(){
        List<Guest> guests = this.controller.getGuestsSortedDescendingBySum();
        assertEquals(guests.get(0).getFinalSum(), 550.87);
        assertEquals(guests.get(7).getFinalSum(),250);
        assertEquals(guests.get(17).getFinalSum(), 0.0);
    }

    @Test
    void testGetAllInstructors() {
        List<Instructor> instructors = controller.getAllInstructors();
        assertEquals(instructors.size(), 6);
        assertEquals(instructors.get(0).getID(), "i1");
        assertEquals(instructors.get(5).getID(), "i6");
    }

    @Test
    void testFindInstructorByUsername(){
        Instructor instructor = this.controller.findInstructorByUsername("i2");
        assertEquals(instructor, controller.getAllInstructors().get(1));
        assertEquals(instructor.getName(), "James John");
        assertEquals(instructor.getPassword(), "qwerty");

        // find instructor with non-existent id
        instructor = this.controller.findInstructorByUsername("i10");
        assertNull(instructor);
    }

    @Test
    void testFindGuestByUsername(){
        Guest guest = this.controller.findGuestByUsername("maria01");
        assertEquals(guest, controller.getAllGuests().get(0));
        assertEquals(guest.getName(), "Maria Kis");
        assertEquals(guest.getPassword(), "KM01");
    }

    @Test
    void testChangeInstructorOfAttraction(){
        Instructor instructor1 = this.controller.findInstructorByUsername("i6");
        Attraction attraction = attractionRepository.getAllAttractions().get(7);
        // in the attraction-list of the instructor appears the attraction and vice versa
        assertEquals(instructor1, attraction.getInstructor());
        assertTrue(instructor1.getAttractions().contains(attraction));

        boolean succesful = false;
        try {
            succesful = this.controller.changeInstructorOfAttraction(attraction.getID(),"i1");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(succesful);
        Instructor instructor2 = this.controller.findInstructorByUsername("i1");
        // in the attraction-list of the instructor the attraction disappeared
        assertFalse(instructor1.getAttractions().contains(attraction));

        // at the same position we have a new instructor for the attraction
        assertEquals(attractionRepository.getAllAttractions().get(7).getInstructor(), instructor2);
        // changes must appear at the side of instructors as well
        assertEquals(instructor2.getAttractions().get(1), attraction);

        // changes must be seen at guests too
        Guest guest = this.controller.findGuestByUsername("maria01");
        assertTrue(attraction.guestList.contains(guest));
        assertEquals(guest.getAttractions().get(0).getInstructor(), instructor2);

        // changing instructor to a new instructor with non-existent id -> not possible
        try {
            succesful = this.controller.changeInstructorOfAttraction(attraction.getID(),"i20");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertFalse(succesful);
    }

    @Test
    void testSignUpForAlreadySignedUpGuest()throws NoMoreAvailableTicketsException{
        // guest already signed up -> sign up again not possible
        Guest guest1 = this.controller.findGuestByUsername("maria01");
        Attraction attraction1 = null;
        try {
            attraction1 = this.controller.getAllAttractions().get(4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(guest1.getAttractions().contains(attraction1));
        assertTrue(attraction1.guestList.contains(guest1));
        boolean successful = this.controller.signUpForAttraction("maria01", attraction1.getID());
        assertFalse(successful);
    }

    @Test
    void testUnSuccesfulsignUpForAttractionWithNoFreePlaces(){
        // sign up when there are no free places -> not possible
        Attraction attraction2 = null;
        try {
            attraction2 = this.controller.getAttractionsSortedByTitle().get(3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(attraction2.getNrOfFreePlaces(), 0);

        Attraction finalAttraction = attraction2;
        Throwable exception = assertThrows(NoMoreAvailableTicketsException.class, () -> this.controller.signUpForAttraction("ioana_maria", finalAttraction.getID()));
        assertEquals(exception.getMessage(), "Wir haben nicht mehr Platz");
    }

    @Test
    void testSuccesfulsignUpForAttraction() throws NoMoreAvailableTicketsException{
        Attraction attraction = null;
        try {
            attraction = this.controller.getAllAttractions().get(4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // successful sign up -> number of free places decreases, sum paid by guests increases
        Guest guest = new Guest("ioana_maria","Ioana", "Maria", "passw123", LocalDate.of(1970,8,10));
        this.controller.addGuest(guest);
        assertEquals(attraction.getNrOfFreePlaces(), 4);
        boolean successful = this.controller.signUpForAttraction("ioana_maria", attraction.getID());
        assertTrue(successful);
        assertEquals(attraction.getNrOfFreePlaces(),3);
        assertEquals(this.controller.getFinalSumOfGuest("ioana_maria"), attraction.price);
    }

    @Test
    void testDeleteAttraction(){
        try {
            assertEquals(this.controller.getAllAttractions().size(), 8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Attraction attraction = null;
        try {
            attraction = this.controller.getAttractionsSortedByTitle().get(3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // delete attraction by another instructor than who holds the attraction -> not possible
        boolean successful = false;
        try {
            successful = this.controller.deleteAttraction("i3", attraction.getID());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertFalse(successful);

        // delete attraction by own instructor -> changes appear at the guests too
        Instructor instructor = this.controller.findInstructorByUsername("i6");
        assertTrue(instructor.getAttractions().contains(attraction));
        try {
            successful = this.controller.deleteAttraction("i6", attraction.getID());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(successful);
        for (Guest guest: attraction.guestList){
            assertFalse(guest.getAttractions().contains(attraction));
        }
        assertFalse(instructor.getAttractions().contains(attraction));
        try {
            assertEquals(this.controller.getAllAttractions().size(), 7);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void filterInstructorsWithHigherSalaryThanTheAverageTest(){
        assertFalse(this.controller.filterInstructorsWithHigherSalaryThanTheAverage().isEmpty());
        assertEquals(this.controller.filterInstructorsWithHigherSalaryThanTheAverage().size(),2);
//        for(Instructor instructor: this.controller.getAllInstructors()){
//            System.out.println(instructor.getFinalSum());
//        }
        Instructor instructorNew = new Instructor("iNew", "first", "last", "passwd");
        instructorNew.setFinalSum(5000.0);
        this.controller.addInstructor(instructorNew);
        assertEquals(this.controller.filterInstructorsWithHigherSalaryThanTheAverage().size(),2);
    }

//    @Test
//    void testfilterAttractionsByAGivenValue() throws NoSuchDataException, IOException {
//        double price = 60.5;
//        Instructor instructor1 = new Instructor("i3", "Lucy", "Misterious", "abc123");
//        Instructor instructor2 = new Instructor("i4", "Katy", "Gal", "123321");
//
//        Attraction attraction1 = new Attraction("Fluffy Animals",10, instructor1,55.00,"A456", Weekday.WEDNESDAY);
//        Attraction attraction2 = new Attraction("White Lion",55, instructor2,60.50,"RQ67", Weekday.SUNDAY);
//        List<Attraction> attractions = this.controller.filterAttractionsByAGivenValue(price);
//        assertEquals(attractions.get(0), attraction1);
//        assertEquals(attractions.get(1), attraction2);
//    }

}
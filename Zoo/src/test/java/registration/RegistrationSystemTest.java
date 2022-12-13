package registration;

import domain.Attraction;
import domain.Guest;
import domain.Instructor;
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

import java.time.LocalDate;
import java.util.ArrayList;
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
        this.guestRepository = new InMemoryGuestRepository(attractionRepository);
        this.controller = new RegistrationSystem(attractionRepository, guestRepository, instructorRepository);
    }

    @Test
    void testGetAllAttractions(){
        List<Attraction> attractions = controller.getAllAttractions();
        assertEquals(attractions.size(), 8);
        assertEquals(attractions.get(0).name, "Zoo time");
        assertEquals(attractions.get(7).name, "VIP zoo show");
    }

    @Test
    void testSuccessfulAddAttraction() {
        // test with existing instructor id
        Attraction attraction1 = new Attraction("Sea lion show", 50, null, 120.4, "Pool1", SUNDAY);
        assertTrue(controller.addAttraction(attraction1, "i2"));

        // add attraction after add new instructor
        Attraction attraction2 = new Attraction("Orca show", 50, null, 100.10, "Pool1", MONDAY);
        Instructor instructor = new Instructor("i10","Samantha", "Thompson", "passwordABC");
        controller.addInstructor(instructor);
        assertTrue(controller.addAttraction(attraction2, "i10"));
    }

    @Test
    void testUnSuccessfulAddAttraction() {
        // test with non-existent instructor id
        Attraction attraction1 = new Attraction("Orca show", 50, null, 100.10, "Pool1", MONDAY);
        assertFalse(controller.addAttraction(attraction1, "i15"));

        // add the same attraction twice
        Attraction attraction2 = controller.getAllAttractions().get(1);
        assertFalse(controller.addAttraction(attraction2, "i2"));
        assertFalse(controller.addAttraction(attraction2, "i3"));
    }

    @Test
    void testGetAllAttractionsWithFreePlaces() throws NoSuchDataException {
        assertEquals(controller.getAllAttractionsWithFreePlaces().size(), 7);
        // attraction with no more free places is not included
        Attraction attractionWithNoMorePlaces = controller.getAttractionsSortedByTitle().get(3);
        assertEquals(attractionWithNoMorePlaces.getNrOfFreePlaces(),0);
        assertFalse(controller.getAllAttractionsWithFreePlaces().contains(attractionWithNoMorePlaces));

        // attraction with more free places is included
        Attraction attractionWithFreePlaces = controller.getAttractionsSortedByTitle().get(2);
        assertNotEquals(attractionWithFreePlaces.getNrOfFreePlaces(),0);
        assertTrue(controller.getAllAttractionsWithFreePlaces().contains(attractionWithFreePlaces));
    }

    @Test
    void testGetAttractionsSortedByTitle() {
        List<Attraction> sortedAttractions = controller.getAttractionsSortedByTitle();
        assertEquals(sortedAttractions.size(),8);
        assertEquals(sortedAttractions.get(0).name, "Angry Panda");
        assertEquals(sortedAttractions.get(7).name, "Zoo time");
    }

    @Test
    void testGetAttractionsAfterAGivenDay(){
        assertEquals(controller.getAttractionsAfterAGivenDay(THURSDAY).size(), 4);
        List<Attraction> attractions = controller.getAttractionsAfterAGivenDay(SUNDAY);
        assertEquals(attractions.size(), 1);
        assertEquals(attractions.get(0), controller.getAllAttractions().get(6));
    }

    @Test
    void testGetGuestsOfAttraction(){
        String idAttraction1 = controller.getAttractionsSortedByTitle().get(3).getID();
        List<Guest> guests = controller.getGuestsOfAttraction(idAttraction1);
        assertEquals(guests.size(), 10);
        Guest guest = controller.findGuestByUsername("leo_dicaprio");
        assertTrue(guests.contains(guest));
        guest = controller.findGuestByUsername("gibson_mel");
        assertFalse(guests.contains(guest));

        // no guests registered for the attraction (empty list)
        String idAttraction2 = controller.getAttractionsSortedByTitle().get(0).getID();
        assertEquals(controller.getGuestsOfAttraction(idAttraction2), new ArrayList<Guest>());
        assertFalse(controller.getGuestsOfAttraction(idAttraction2).contains(guest));
    }

    @Test
    void testSuccessfulAddGuest() {
        // add a new guest with unique username
        Guest guest1 = new Guest("ioana.petru2","Ioana", "Petru",  "123abc", LocalDate.of(1978,2,2));
        assertTrue(controller.addGuest(guest1));

        // add a new guest with same attributes as existing one (apart from username)
        Guest guest2 = new Guest("pop.otilia","Otilia", "Pop", "animals", LocalDate.of(2002,4,11));
        assertTrue(controller.addGuest(guest2));
    }

    @Test
    void testUnSuccessfulAddGuest() {
        // add a new guest with existing username -> not possible
        Guest guest1 = new Guest("ioana.petru","Ioana", "Petru",  "123abc", LocalDate.of(1978,2,2));
        assertFalse(controller.addGuest(guest1));

        // add a guest twice -> not possible
        Guest guest2 = controller.findGuestByUsername("ioana.petru");
        assertNotNull(guest2);
        assertFalse(controller.addGuest(guest2));
    }

    @Test
    void testSuccessfulAddInstructor() {
        // add instructor with unique id
        Instructor instructor1 = new Instructor("i10","Samantha", "Thompson", "passwordABC");
        assertTrue(controller.addInstructor(instructor1));
    }

    @Test
    void testUnSuccessfulAddInstructor() {
        // add instructor with existing id -> not possible
        Instructor instructor2 = new Instructor("i4","John", "Simpson", "1234");
        assertFalse(controller.addInstructor(instructor2));

        // add instructor twice -> not possible
        Instructor instructor1 = controller.findInstructorByUsername("i3");
        assertNotNull(instructor1);
        assertFalse(controller.addInstructor(instructor1));
    }

    @Test
    void testGetAllGuests() {
        List<Guest> guests = this.controller.getAllGuests();
        assertEquals(guests.size(), 18);
        assertEquals(guests.get(0).getID(), "maria01");
        assertEquals(guests.get(17).getID(), "terence_hill");
    }

    @Test
    void testGetAttractionsOfGuest() {
        // test with a guest who signed up for 2 attractions
        List<Attraction> guestAttractions = controller.getAttractionsOfGuest("maria01");
        assertEquals(guestAttractions.size(), 2);

        List<Attraction> attractions = controller.getAllAttractions();
        assertTrue(guestAttractions.contains(attractions.get(7)));
        assertTrue(guestAttractions.contains(attractions.get(4)));
        assertFalse(guestAttractions.contains(attractions.get(5)));

        // guest who is not registered to any attractions
        guestAttractions = controller.getAttractionsOfGuest("celined");
        assertEquals(guestAttractions.size(), 0);
        assertEquals(guestAttractions, new ArrayList<Attraction>());
    }

    @Test
    void testGetFinalSumOfGuest() throws NoMoreAvailableTicketsException{
        // under 18 -> ticket with discount
        assertEquals(this.controller.getFinalSumOfGuest("katy99"),150.435);
        // above 60 -> ticket with discount
        assertEquals(this.controller.getFinalSumOfGuest("leo_dicaprio"),240.696, 0.1);

        assertEquals(this.controller.getFinalSumOfGuest("maria01"),550.87);
        Attraction attraction = this.controller.getAllAttractions().get(0);
        String idAttraction = this.controller.getAllAttractions().get(0).getID();
        Instructor instructor = attraction.getInstructor();
        double sumInstructor = instructor.getFinalSum();
        assertEquals(sumInstructor,0);
        assertEquals(attraction.getNrOfGuests(),0);

        // sign up for attraction -> sum increases
        assertTrue(this.controller.signUpForAttraction("maria01", idAttraction));
        assertEquals(this.controller.getFinalSumOfGuest("maria01"),731.86);
        // instructor gets more money
        assertEquals(instructor.getFinalSum(), sumInstructor + attraction.price);

        // attraction deleted -> sum increases (at guest and instructor too)
        this.controller.deleteAttraction("i1",idAttraction);
        assertEquals(this.controller.getFinalSumOfGuest("maria01"),550.87);
        assertEquals(instructor.getFinalSum(), sumInstructor);
    }

    @Test
    void testGetSumFromGuests() {
        // guest who did not sign up for attractions
        assertEquals(this.controller.getSumFromGuests("i2"),0);
        // guest who is going to attend more attractions
        assertEquals(this.controller.getSumFromGuests("i6"),3008.7);
    }

    @Test
    void testGetGuestsSortedDescendingBySum() {
        List<Guest> guests = this.controller.getGuestsSortedDescendingBySum();
        assertEquals(guests.size(),18);
        System.out.println(guests.get(0).getFinalSum());
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

        // find guest with non-existent id
        guest = this.controller.findGuestByUsername("janet_j");
        assertNull(guest);
    }

    @Test
    void testChangeInstructorOfAttraction(){
        Instructor instructor1 = this.controller.findInstructorByUsername("i6");
        Attraction attraction = attractionRepository.getAllAttractions().get(7);
        // in the attraction-list of the instructor appears the attraction and vice versa
        assertEquals(instructor1, attraction.getInstructor());
        assertTrue(instructor1.getAttractions().contains(attraction));

        boolean succesful = this.controller.changeInstructorOfAttraction(attraction.getID(),"i1");
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
        succesful = this.controller.changeInstructorOfAttraction(attraction.getID(),"i20");
        assertFalse(succesful);
    }

    @Test
    void testSignUpForAlreadySignedUpGuest()throws NoMoreAvailableTicketsException{
        // guest already signed up -> sign up again not possible
        Guest guest1 = this.controller.findGuestByUsername("maria01");
        Attraction attraction1 = this.controller.getAllAttractions().get(4);
        assertTrue(guest1.getAttractions().contains(attraction1));
        assertTrue(attraction1.guestList.contains(guest1));
        boolean successful = this.controller.signUpForAttraction("maria01", attraction1.getID());
        assertFalse(successful);
    }

    @Test
    void testUnSuccesfulSignUpForAttractionWithNoFreePlaces(){
        // sign up when there are no free places -> not possible
        Attraction attraction2 = this.controller.getAttractionsSortedByTitle().get(3);
        assertEquals(attraction2.getNrOfFreePlaces(), 0);

        assertFalse(this.controller.signUpForAttraction("ioana_maria", attraction2.getID()));
    }

    @Test
    void testSuccesfulSignUpForAttraction() throws NoMoreAvailableTicketsException{
        Attraction attraction = this.controller.getAllAttractions().get(4);
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
    void testDeleteAttraction() {
        assertEquals(this.controller.getAllAttractions().size(), 8);
        Attraction attraction = this.controller.getAttractionsSortedByTitle().get(3);

        // delete attraction by another instructor than who holds the attraction -> not possible
        boolean successful = this.controller.deleteAttraction("i3", attraction.getID());
        assertFalse(successful);

        // delete attraction by own instructor -> changes appear at the guests too
        Instructor instructor = this.controller.findInstructorByUsername("i6");
        assertTrue(instructor.getAttractions().contains(attraction));
        successful = this.controller.deleteAttraction("i6", attraction.getID());
        assertTrue(successful);
        for (Guest guest: attraction.guestList){
            assertFalse(guest.getAttractions().contains(attraction));
        }
        assertFalse(instructor.getAttractions().contains(attraction));
        assertEquals(this.controller.getAllAttractions().size(), 7);
    }

    @Test
    void testGetAttractionsSortedByPriceAscending() {
        List<Attraction> sortedAttractionsByPrice = controller.getAttractionsSortedByPriceAscending();
        assertEquals(sortedAttractionsByPrice.get(0).price,55.00);
        assertEquals(sortedAttractionsByPrice.get(7).price,300.87);
    }

    @Test
    void testGetAttractionsSortedByGuestAscending() {
        List<Attraction> sortedAttractionsByNrOfGuests = controller.getAttractionsSortedByGuestAscending();
        assertEquals(sortedAttractionsByNrOfGuests.get(7).getNrOfGuests(), 10);
        assertEquals(sortedAttractionsByNrOfGuests.get(0).getNrOfGuests(), 0);
    }

    @Test
    void testFilterAttractionsByAGivenValue() throws NoSuchDataException {
        List<Attraction> sortedAttractions = controller.filterAttractionsByAGivenValue(99.99);
        assertEquals(sortedAttractions.size(),4);
    }

    @Test
    void testNotFoundDataFilterAttractionsByAGivenValue(){
        // no matching attractions
        List<Attraction> attrWithPriceLessThan10 = this.controller.filterAttractionsByAGivenValue(10);
        assertEquals(attrWithPriceLessThan10.size(),0);
    }

    @Test
    void testGetAverageSalaryOfInstructors() {
        assertEquals(controller.getAverageSalaryOfInstructors(),668.116,0.1);
    }

    @Test
    void testFilterInstructorsWithHigherSalaryThanAverage() {
        List<Instructor> instructorsWithHighSalary = controller.filterInstructorsWithHigherSalaryThanAverage();
        assertEquals(instructorsWithHighSalary.size(),2);

        // both instructors have higher salary than the average (668.116)
        Instructor instructor = instructorsWithHighSalary.get(0);
        assertEquals(instructor.getFinalSum(), 1000);

        instructor = instructorsWithHighSalary.get(1);
        assertEquals(instructor.getFinalSum(), 3008.7);

        // any other instructor have lower salary
        instructor = controller.getAllInstructors().get(2);
        assertFalse(instructorsWithHighSalary.contains(instructor));
        assertEquals(instructor.getFinalSum(), 0);

    }
}
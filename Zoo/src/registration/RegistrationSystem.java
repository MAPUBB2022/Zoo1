package registration;
import domain.Attraction;
import domain.Guest;
import domain.Instructor;
import domain.Weekday;
import repository.AttractionRepository;
import repository.GuestRepository;
import repository.InstructorRepository;
import utils.NoMoreAvailableTicketsException;
import utils.NoSuchDataException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RegistrationSystem {
    private final AttractionRepository attractionRepository;
    private final GuestRepository guestRepository;
    private final InstructorRepository instructorRepository;

    public RegistrationSystem(AttractionRepository attractionRepository, GuestRepository guestRepository, InstructorRepository instructorRepository) {
        this.attractionRepository = attractionRepository;
        this.guestRepository = guestRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<Attraction> getAllAttractions() throws IOException {
        return this.attractionRepository.getAllAttractions();
    }

    public boolean addAttraction(Attraction attraction, String idInstructor){
        Instructor instructor = this.findInstructorByUsername(idInstructor);
        if (instructor != null){
            attraction.setInstructor(instructor);
            this.attractionRepository.add(attraction);
            // attraction must appear at the attraction list of the instructor too
            instructor.addAttraction(attraction);
            this.instructorRepository.update(instructor, instructor.getID());
            return true;
        }
        return false;
    }

    public List<Attraction> getAllAttractionsWithFreePlaces() throws IOException {
        List<Attraction> attractionsWithFreePlaces = new ArrayList<>();
        for (Attraction attraction: this.attractionRepository.getAllAttractions())
            if (attraction.getCapacity() > attraction.getNrOfGuests())
                attractionsWithFreePlaces.add(attraction);
        return attractionsWithFreePlaces;
    }

    public List<Attraction> getAttractionsSortedByTitle() throws IOException {
        List<Attraction> sortedAttractions = this.attractionRepository.getAllAttractions();
        Collections.sort(sortedAttractions);
        return sortedAttractions;
    }

    public List<Attraction> getAttractionsAfterAGivenDay(Weekday weekday) throws IOException {
        List<Attraction> attractionsAfterADay = new ArrayList<>();
        for (Attraction attr: this.attractionRepository.getAllAttractions()){
            if (attr.day.getNr() >= weekday.getNr())
                attractionsAfterADay.add(attr);
        }
        return attractionsAfterADay;
    }

    public List<Guest> getGuestsOfAttraction(String idAttraction) throws IOException {
        Attraction attraction = this.attractionRepository.findByID(idAttraction);
        if (attraction != null)
            return attraction.guestList;
        else
            return null;
    }

    public boolean addGuest(Guest guest){
        int guestsInitialNr = this.guestRepository.getAllGuests().size();
        this.guestRepository.add(guest);
        return this.guestRepository.getAllGuests().size() == guestsInitialNr + 1;
    }

    public List<Guest> getAllGuests() {
        return this.guestRepository.getAllGuests();
    }

    public List<Attraction> getAttractionsOfGuest(String idGuest){
        Guest guest = this.guestRepository.findByID(idGuest);
        if (guest!=null)
            return guest.getAttractions();
        else
            return null;
    }

    public double getFinalSumOfGuest(String idGuest){
        Guest guest = this.guestRepository.findByID(idGuest);
        if (guest!=null)
            return guest.getFinalSum();
        return 0;
    }

    public List<Guest> getGuestsSortedDescendingBySum(){
        List<Guest> guests = this.guestRepository.getAllGuests();
        guests.sort(Collections.reverseOrder());
        return guests;
    }

    public Guest findGuestByUsername(String username){
        return this.guestRepository.findByID(username);
    }

    public Instructor findInstructorByUsername(String username){
        return this.instructorRepository.findByID(username);
    }

    public boolean addInstructor(Instructor instructor){
        int instructorsInitialNr = this.instructorRepository.getAllInstructors().size();
        this.instructorRepository.add(instructor);
        return this.instructorRepository.getAllInstructors().size() == instructorsInitialNr + 1;
    }

    public List<Instructor> getAllInstructors() {
        return this.instructorRepository.getAllInstructors();
    }

    public double getSumFromGuests(String idInstructor){
        Instructor instructor = this.findInstructorByUsername(idInstructor);
        return instructor.getFinalSum();
    }
    public boolean changeInstructorOfAttraction(String idAttraction, String idNewInstructor) throws IOException {
        Attraction attr = this.attractionRepository.findByID(idAttraction);
        Instructor newInstructor = this.instructorRepository.findByID(idNewInstructor);
        if (attr != null && newInstructor != null) {
            Instructor oldInstructor = attr.getInstructor();
            oldInstructor.removeAttraction(attr);
            attr.setInstructor(newInstructor);
            newInstructor.addAttraction(attr);
            return true;
        }
        return false;
    }

    public boolean signUpForAttraction(String idGuest, String idAttraction) throws NoMoreAvailableTicketsException{
        Attraction attr = this.attractionRepository.findByID(idAttraction);
        if (attr!= null)
        {
            if (attr.getNrOfFreePlaces() > 0){
                Guest g = this.guestRepository.findByID(idGuest);
                // if guest is already signed up -> sign up not possible
                if (g!=null && !attr.guestList.contains(g))
                {
                    g.addAttraction(attr);
                    attr.addGuest(g);
                    attr.getInstructor().calculateSum();
                    return true;
                }
            }
            else throw new NoMoreAvailableTicketsException("Wir haben nicht mehr Platz");
        }
        return false;
    }

    public boolean deleteAttraction (String idInstructor, String idAttraction) throws IOException {
        Attraction attr = this.attractionRepository.findByID(idAttraction);
        if (attr!=null && attr.getInstructor().getID().equals(idInstructor)){
            Instructor instructor = this.instructorRepository.findByID(idInstructor);
            instructor.removeAttraction(attr);
            this.attractionRepository.delete(idAttraction);

            for (Guest guest: this.guestRepository.getAllGuests()){
                guest.removeAttraction(attr);
                this.guestRepository.update(guest, guest.getID());
            }
            return true;
        }
        return false;
    }

    public List<Attraction> filterAttractionsByAGivenValue(double preis) throws IOException, NoSuchDataException {
        boolean found = false;
        for(Attraction a: getAllAttractions()){
            if(a.price <= preis) found = true;
        }
        if(found == true)
        {
            List<Attraction> filteredAttractions = new ArrayList<>();
            getAllAttractions().stream().filter(a->a.price <= preis).forEach(a->filteredAttractions.add(a));
            return filteredAttractions;
        }
        else throw new NoSuchDataException("Es gibt keine Attraktion unter und mit dieser Preis");
    }

    public double calculateAverageSalaryOfInstructors(){
        double salary = 0;
        for(Instructor instructor: getAllInstructors()){
            salary = salary + instructor.getFinalSum();
        }
        return salary/getAllInstructors().size();
    }

    public List<Instructor> filterInstructorsWithHigherSalaryThanTheAverage() {
        List<Instructor> instructors = getAllInstructors().stream()
                .filter(i -> i.getFinalSum() > calculateAverageSalaryOfInstructors()).collect(Collectors.toList());
        return instructors;
    }

    public boolean instructorExistsOrNot(Instructor instructor){
        return instructor.ExistsOrNot();
    }

    public boolean attractionExistsOrNot(Attraction attraction){
        return attraction.ExistsOrNot();
    }

    public boolean guestExistsOrNot(Guest guest){
        return guest.ExistsOrNot();
    }
}
package registration;

import domain.Attraction;
import domain.Guest;
import domain.Instructor;
import domain.Weekday;
import repository.AttractionRepository;
import repository.GuestRepository;
import repository.InstructorRepository;
import utils.NoMoreAvailableTicketsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistrationSystem {
    private final AttractionRepository attractionRepository;
    private final GuestRepository guestRepository;
    private final InstructorRepository instructorRepository;

    public RegistrationSystem(AttractionRepository attractionRepository, GuestRepository guestRepository, InstructorRepository instructorRepository) {
        this.attractionRepository = attractionRepository;
        this.guestRepository = guestRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<Attraction> getAllAttractions(){
        return this.attractionRepository.getAllAttractions();
    }

    public boolean addAttraction(Attraction attraction, String idInstructor){
        Instructor instructor = this.findInstructorByUsername(idInstructor);
        if (instructor != null){
            attraction.setInstructor(instructor);
            this.attractionRepository.add(attraction);
            // attraction must appear at the attraction list of the instructor too
            instructor.addAttraction(attraction);
            this.instructorRepository.update(instructor.getID(), instructor);
            return true;
        }
        return false;
    }

    public List<Attraction> getAllAttractionsWithFreePlaces(){
        List<Attraction> attractionsWithFreePlaces = new ArrayList<>();
        for (Attraction attraction: this.attractionRepository.getAllAttractions())
            if (attraction.getCapacity() > attraction.getNrOfGuests())
                attractionsWithFreePlaces.add(attraction);
        return attractionsWithFreePlaces;
    }

    public List<Attraction> getAttractionsSortedByTitle() {
        List<Attraction> sortedAttractions = this.attractionRepository.getAllAttractions();
        Collections.sort(sortedAttractions);
        return sortedAttractions;
    }

    public List<Attraction> getAttractionsAfterAGivenDay(Weekday weekday){
        List<Attraction> attractionsAfterADay = new ArrayList<>();
        for (Attraction attr: this.attractionRepository.getAllAttractions()){
            if (attr.day.getNr() >= weekday.getNr())
                attractionsAfterADay.add(attr);
        }
        return attractionsAfterADay;
    }

    public List<Guest> getGuestsOfAttraction(String idAttraction){
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
    public boolean changeInstructorOfAttraction(String idAttraction, String idNewInstructor) {
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

    public boolean deleteAttraction (String idInstructor, String idAttraction){
        Attraction attr = this.attractionRepository.findByID(idAttraction);
        if (attr!=null && attr.getInstructor().getID().equals(idInstructor)){
            Instructor instructor = this.instructorRepository.findByID(idInstructor);
            instructor.removeAttraction(attr);
            this.attractionRepository.delete(idAttraction);

            for (Guest guest: this.guestRepository.getAllGuests()){
                guest.removeAttraction(attr);
                this.guestRepository.update(guest.getID(),guest);
            }
            return true;
        }
        return false;
    }
}
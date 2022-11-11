package registration;

import domain.Attraction;
import domain.Guest;
import domain.Instructor;
import domain.Weekday;
import repository.AttractionRepository;
import repository.GuestRepository;
import repository.InstructorRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RegistrationSystem {
    private AttractionRepository attractionRepository;
    private GuestRepository guestRepository;
    private InstructorRepository instructorRepository;

    public RegistrationSystem(AttractionRepository attractionRepository, GuestRepository guestRepository, InstructorRepository instructorRepository) {
        this.attractionRepository = attractionRepository;
        this.guestRepository = guestRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<Attraction> getAllAttractions(){
        return this.attractionRepository.getAllAttractions();
    }

    public boolean addAttraction(Attraction attraction){
        attractionRepository.add(attraction);
        return true;
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
        //Collections.sort(sortedAttractions);
        return sortedAttractions;
    }

    public List<Attraction> getAttractionsAfterAGivenDay(Weekday weekday){
        List<Attraction> attractionsAfterADay = new ArrayList<>();
        for (Attraction attr: this.attractionRepository.getAllAttractions()){
            if (attr.day.getNr() > weekday.getNr())
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

    public List<Guest> getAllGuests() {
        return this.guestRepository.getAllGuests();
    }

    public List<Attraction> getAttractionsOfGuest(String idGuest){
        Guest guest = this.guestRepository.findByID(idGuest);
        if (guest!=null)
            return guest.attractions;
        else
            return null;
    }

    double getFinalSumOfGuest(String idGuest){
        Guest guest = this.guestRepository.findByID(idGuest);
        if (guest!=null)
            return guest.getFinalSum();
        return 0;
        // throw Exception
    }

    List<Guest> getGuestsSortedDescendingBySum(){
        List<Guest> guests = this.guestRepository.getAllGuests();
        guests.sort(Collections.reverseOrder());
        return guests;
    }

    public List<Instructor> getAllInstructors() {
        return this.instructorRepository.getAllInstructors();
    }

    public boolean signUpForAttraction(String idGuest, String idAttraction, LocalDate date){
        Attraction attr = this.attractionRepository.findByID(idAttraction);
        if (attr!= null && attr.getNrOfFreePlaces() > 0)
        {   // check date too
            Guest g = this.guestRepository.findByID(idGuest);
            if (g!=null)
            {
                g.attractions.add(attr);
                g.calculateSum();
                this.guestRepository.update(g, idGuest);
                attr.guestList.add(g);
                this.attractionRepository.update(attr, idAttraction);
                return true;
            }
        }
        return false;
    }

    public boolean deleteAttraction (String idInstructor, String idAttraction){
        Attraction attr = this.attractionRepository.findByID(idAttraction);
        if (attr!=null && attr.instructor.getID().equals(idInstructor)){
            Instructor instructor = this.instructorRepository.findByID(idInstructor);
            instructor.attractionOfInstructor.remove(attr);
            this.instructorRepository.update(instructor,idInstructor);

            for (Guest guest: this.guestRepository.getAllGuests()){
                guest.attractions.remove(attr);
                guest.calculateSum();
                this.guestRepository.update(guest,idAttraction);
            }
            return true;
        }
        return false;
    }
}
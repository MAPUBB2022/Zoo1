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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        // instructor with the given ID must exist
        // attraction doesn't appear previously in the list of attractions
        if (instructor != null && this.attractionRepository.findByID(attraction.getID()) == null){
            attraction.setInstructor(instructor);
            this.attractionRepository.add(attraction);
            // attraction must appear at the attractionlist of the instructor too
            instructor.addAttraction(attraction);
            this.instructorRepository.update(instructor, instructor.getID());
            return true;
        }
        return false;
    }

    public List<Attraction> getAllAttractionsWithFreePlaces(){
        List<Attraction> attractionsWithFreePlaces = new ArrayList<>();
        for (Attraction attraction: this.attractionRepository.getAllAttractions())
            if (attraction.getCapacity() > attraction.getNrOfGuests())
                attractionsWithFreePlaces.add(attraction);
        if (attractionsWithFreePlaces.size() == 0)
            return null;
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

    public List<Attraction> getAttractionsSortedByPriceAscending() {
        List<Attraction> sortedAttractions = this.attractionRepository.getAllAttractions();
        Collections.sort(sortedAttractions,
                (Attraction a1, Attraction a2) -> Double.compare(a1.price, a2.price));
        /*
        Collections.sort(sortedAttractions, new Comparator<Attraction>() {
            @Override
            public int compare(Attraction a1, Attraction a2) {
                return Double.compare(a1.price, a2.price);
            }
        }); */
        return sortedAttractions;
    }

    public List<Attraction> getAttractionsSortedByGuestAscending() {
        List<Attraction> sortedAttractions = this.attractionRepository.getAllAttractions();
        Collections.sort(sortedAttractions,
                (Attraction o1, Attraction o2) -> {return o1.getNrOfGuests() - o2.getNrOfGuests();}
        );
        /*
        Collections.sort(sortedAttractions, new Comparator<Attraction>() {
            @Override
            public int compare(Attraction o1, Attraction o2) { return o1.getNrOfGuests() - o2.getNrOfGuests();     }
        }); */
        return sortedAttractions;
    }

    public List<Attraction> filterAttractionsByAGivenValue(double price) throws NoSuchDataException {
        List<Attraction> attractionsWithFixedPrice = new ArrayList<>();
        for (Attraction attraction: this.attractionRepository.getAllAttractions())
            if (attraction.price <=  price)
                attractionsWithFixedPrice.add(attraction);

        if (attractionsWithFixedPrice.size() == 0)
            throw new NoSuchDataException("Keine Attraktionen gefunden");
        return attractionsWithFixedPrice;
    }

    public List<Guest> getGuestsOfAttraction(String idAttraction){
        Attraction attraction = this.attractionRepository.findByID(idAttraction);
        if (attraction != null)
            return attraction.guestList;
        else
            return null;
    }

    public double getAverageSalaryOfInstructors(){
        double avgSum = 0;
        for (Instructor instructor: this.getAllInstructors()){
            avgSum += instructor.getFinalSum();
        }
        avgSum = avgSum / this.getAllInstructors().size();
        return avgSum;
    }

    public List<Instructor> filterInstructorsWithHigherSalaryThanAverage(){
        List<Instructor> instructorsWithHighSalary = new ArrayList<>();
        double avgSum = this.getAverageSalaryOfInstructors();
        for (Instructor instructor: this.instructorRepository.getAllInstructors())
            if(instructor.getFinalSum() > avgSum)
                instructorsWithHighSalary.add(instructor);
        return instructorsWithHighSalary;
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
        Collections.sort(guests, new Comparator<Guest>() {
            @Override
            public int compare(Guest o1, Guest o2) {
                if (o1.getFinalSum() < o2.getFinalSum()) return 1;
                else if (o1.getFinalSum() == o2.getFinalSum()) return 0;
                else return -1;
            }
        });
        // guests.sort(Collections.reverseOrder());
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
                this.guestRepository.update(guest, guest.getID());
            }
            return true;
        }
        return false;
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

    public boolean validateDouble(String input){
        try{
            double result = Double.parseDouble(input);
        }
        catch (NoSuchDataException e){
            e.getMessage();
            System.out.println("Dein Input ist inkorrekt. ");
            return false;
        }
        return true;
    }

    public boolean validateInt(String input){
        try{
            int result = Integer.parseInt(input);
        }
        catch(NoSuchDataException e){
            e.getMessage();
            System.out.println("Dein Input ist inkorrekt. ");
            return false;
        }
        return true;
    }
}
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
import utils.BadInputException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public List<Attraction> getAllAttractions() {
        return this.attractionRepository.getAllAttractions();
    }

    public boolean addAttraction(Attraction attraction, String idInstructor) {
        Instructor instructor = this.findInstructorByUsername(idInstructor);
        // instructor with the given ID must exist
        // attraction doesn't appear previously in the list of attractions
        if (instructor != null && attraction!= null && this.attractionRepository.findByID(attraction.getID()) == null) {
            attraction.setInstructor(instructor);
            this.attractionRepository.add(attraction);
            // attraction must appear at the attractionlist of the instructor too
            instructor.addAttraction(attraction);
            this.instructorRepository.update(instructor.getID(), instructor);
            return true;
        }
        return false;
    }

    public List<Attraction> getAllAttractionsWithFreePlaces() {
        List<Attraction> attractionsWithFreePlaces = this.attractionRepository.getAllAttractions()
                .stream()
                .filter(attr -> attr.getCapacity() > attr.getNrOfGuests())
                .toList();

        if (attractionsWithFreePlaces.size() == 0)
            try {
                throw new NoSuchDataException("Keine Attraktionen gefunden");
            } catch (NoSuchDataException e) {
                System.out.println(e.getMessage());
            }
        return attractionsWithFreePlaces;

//        without stream:
//        List<Attraction> attractionsWithFreePlaces = new ArrayList<>();
//        for (Attraction attraction : this.attractionRepository.getAllAttractions())
//            if (attraction.getCapacity() > attraction.getNrOfGuests())
//                attractionsWithFreePlaces.add(attraction);

    }

    public List<Attraction> getAttractionsSortedByTitle() {
        List<Attraction> sortedAttractions = this.attractionRepository.getAllAttractions();
        Collections.sort(sortedAttractions);
        return sortedAttractions;
    }

    public List<Attraction> getAttractionsAfterAGivenDay(Weekday weekday) {
        List<Attraction> attractionsAfterADay = new ArrayList<>();
        try {
            attractionsAfterADay = this.attractionRepository.getAllAttractions()
                    .stream()
                    .filter(attr -> attr.day.getNr() >= weekday.getNr())
                    .toList();
            } catch (NullPointerException ignored) {}
        return attractionsAfterADay;

//        without stream:
//            for (Attraction attr : this.attractionRepository.getAllAttractions()) {
//                if (attr.day.getNr() >= weekday.getNr())
//                    attractionsAfterADay.add(attr);
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

    public List<Attraction> filterAttractionsByAGivenValue(double givenPrice) {
        List<Attraction> attractionsWithFixedPrice =
                this.attractionRepository.getAllAttractions()
                        .stream()
                        .filter(attr -> attr.price <= givenPrice)
                        .toList();

        if (attractionsWithFixedPrice.size() == 0)
            try {
                throw new NoSuchDataException("Keine Attraktionen gefunden");
            } catch (NoSuchDataException e) {
                System.out.println(e.getMessage());
            }
        return attractionsWithFixedPrice;
//      without stream:
//      List<Attraction> attractionsWithFixedPrice2 = new ArrayList<>();
//      for (Attraction attraction : this.attractionRepository.getAllAttractions())
//            if (attraction.price <= givenPrice)
//                attractionsWithFixedPrice2.add(attraction);

    }

    public List<Guest> getGuestsOfAttraction(String idAttraction) {
        Attraction attraction = this.attractionRepository.findByID(idAttraction);
        if (attraction != null)
            return attraction.guestList;
        else
            return null;
    }

    public double getAverageSalaryOfInstructors() {
        double avgSum = 0;
        for (Instructor instructor: this.getAllInstructors()) {
            avgSum += instructor.getFinalSum();
        }
        avgSum = avgSum / this.getAllInstructors().size();
        return avgSum;
    }

    public List<Instructor> filterInstructorsWithHigherSalaryThanAverage() {
        double avgSum = this.getAverageSalaryOfInstructors();
        List<Instructor> instructorsWithHighSalary = this.instructorRepository.getAllInstructors()
                .stream()
                .filter(i -> i.getFinalSum() > avgSum)
                .toList();
        return instructorsWithHighSalary;

        // List<Instructor> instructorsWithHighSalary = new ArrayList<>();
//        double avgSum = this.getAverageSalaryOfInstructors();
//        for (Instructor instructor : this.instructorRepository.getAllInstructors())
//            if (instructor.getFinalSum() > avgSum)
//                instructorsWithHighSalary.add(instructor);
    }

    public boolean addGuest(Guest guest) {
        int guestsInitialNr = this.guestRepository.getAllGuests().size();
        this.guestRepository.add(guest);
        return this.guestRepository.getAllGuests().size() == guestsInitialNr + 1;
    }

    public List<Guest> getAllGuests() {
        return this.guestRepository.getAllGuests();
    }

    public List<Attraction> getAttractionsOfGuest(String idGuest) {
        List<Attraction> attractions = null;
        Guest guest = this.guestRepository.findByID(idGuest);
        try {
            attractions = guest.getAttractions();
            if (attractions.size() == 0)
                throw new NoSuchDataException("Keine Attraktionen gefunden");
        } catch (NoSuchDataException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Besucher mit dem angegebenen Benutzername existiert nicht");
        }
        return attractions;
    }

    public List<Attraction> getAttractionsOfInstructor(String idInstructor) {
        List<Attraction> attractions = null;
        Instructor instructor = this.instructorRepository.findByID(idInstructor);
        try {
            attractions = instructor.getAttractions();
            if (attractions.size() == 0)
                throw new NoSuchDataException("Keine Attraktionen gefunden");
        } catch (NoSuchDataException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Instruktor mit dem angegebenen Benutzername existiert nicht");
        }
        return attractions;
    }

    public double getFinalSumOfGuest(String idGuest) {
        Guest guest = this.guestRepository.findByID(idGuest);
        if (guest != null)
            return guest.getFinalSum();
        return 0;
    }

    public List<Guest> getGuestsSortedDescendingBySum() {
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

    public Guest findGuestByUsername(String username) {
        return this.guestRepository.findByID(username);
    }

    public Instructor findInstructorByUsername(String username) {
        return this.instructorRepository.findByID(username);
    }

    public boolean addInstructor(Instructor instructor) {
        int instructorsInitialNr = this.instructorRepository.getAllInstructors().size();
        this.instructorRepository.add(instructor);
        return this.instructorRepository.getAllInstructors().size() == instructorsInitialNr + 1;
    }

    public List<Instructor> getAllInstructors() {
        return this.instructorRepository.getAllInstructors();
    }

    public double getSumFromGuests(String idInstructor) {
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

    public boolean signUpForAttraction(String idGuest, String idAttraction) {
        Attraction attr = this.attractionRepository.findByID(idAttraction);
        if (attr != null) {
            if (attr.getNrOfFreePlaces() > 0) {
                Guest g = this.guestRepository.findByID(idGuest);
                // if guest is already signed up -> sign up not possible
                if (g != null && !attr.guestList.contains(g))
                {
                    g.addAttraction(attr);
                    attr.addGuest(g);
                    attr.getInstructor().calculateSum();
                    return true;
                }
            } else try {
                throw new NoMoreAvailableTicketsException("Wir haben nicht mehr Platz");
            } catch (NoMoreAvailableTicketsException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean deleteAttraction(String idInstructor, String idAttraction) {
        Attraction attr = this.attractionRepository.findByID(idAttraction);
        if (attr != null && attr.getInstructor().getID().equals(idInstructor)) {
            Instructor instructor = this.instructorRepository.findByID(idInstructor);
            instructor.removeAttraction(attr);
            this.attractionRepository.delete(idAttraction);

            for (Guest guest: this.guestRepository.getAllGuests()) {
                guest.removeAttraction(attr);
                this.guestRepository.update(guest.getID(), guest);
            }
            return true;
        }
        return false;
    }

    public Guest verifiedUserInputGuest(String username, String firstName, String lastName, String birthday, String password) {
        boolean correctInput = this.verifyUserInputNameAndPassword(username, firstName, lastName, password);
        if (correctInput){
            try {
                LocalDate birthdayDate = LocalDate.parse(birthday);
                return new Guest(username, firstName, lastName, password, birthdayDate);
            } catch (DateTimeParseException e){
                System.out.println("Gib das Geburtsdatum im Format Jahr-Monat-Tag an");
            }
        }
        return null;
    }

    public boolean verifyUserInputNameAndPassword(String username, String firstName, String lastName, String password) {
        boolean correctInput = true;
        try {
            if (!username.toLowerCase().equals(username))
                throw new BadInputException("Der Benutzername kann nur kleine Buchstaben enthalten");
            if ((firstName + lastName).matches(".*[@#$%^&*0-9].*"))
                throw new BadInputException("Der Nachname und Vorname kann nur Buchstaben enthalten");
            if (password.length() < 3)
                throw new BadInputException("Das Passwort muss mindestens 3 Character enthalten");

        } catch (BadInputException e) {
            System.out.println(e.getMessage());
            correctInput = false;
        }
        return correctInput;
    }

    public Weekday verifiedUserInputWeekday(String day){
        Weekday weekday = null;
        try {
            weekday = Weekday.valueOf(day.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Der Tag kann sein: Monday, Tuesday, Wednesday, Thursdau, Friday, Saturday oder Sunday");
        }
        return weekday;
    }

    public double verifiedUserInputPrice(String price){
        double priceAttr = 0.0;
        try {
            priceAttr = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            System.out.println("Das Preis muss eine rationale Zahl sein");
        }
        return priceAttr;
    }

    public Attraction verifiedUserInputAttraction(String name, String capacity, String price, String location, String weekday) {
        boolean correctInput = true;
        int capacityAttr = 0;
        try {
            capacityAttr = Integer.parseInt(capacity);
        } catch (NumberFormatException e) {
            System.out.println("Die Kapazität muss eine ganze Zahl sein");
            correctInput = false;
        }

        Weekday weekdayAttr = this.verifiedUserInputWeekday(weekday);
        double priceAttr = this.verifiedUserInputPrice(price);
        if (weekdayAttr == null || priceAttr == 0.0)
            correctInput = false;

        if (correctInput)
            return new Attraction(name, capacityAttr, null, priceAttr, location, weekdayAttr);
        return null;
    }

}
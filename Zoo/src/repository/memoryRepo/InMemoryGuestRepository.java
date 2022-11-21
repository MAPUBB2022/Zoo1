package repository.memoryRepo;

import domain.Attraction;
import domain.Guest;
import repository.AttractionRepository;
import repository.GuestRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InMemoryGuestRepository implements GuestRepository {
    private List<Guest> allGuests;
    private final AttractionRepository attractionRepository;
    private List<Attraction> favourites;

    public List<Attraction> getFavourites() {
        return favourites.stream().sorted().toList();
    }

    public void setFavourites(List<Attraction> favourites) {
        this.favourites = favourites;
    }

    public void addToFavourites(Attraction attraction){
        this.favourites.add(attraction);
    }

    public void removeFromFavourites(Attraction attraction){
        this.favourites.remove(attraction);
    }

    public InMemoryGuestRepository(List<Guest> allGuests, AttractionRepository attractionRepository) throws IOException {
        this.attractionRepository = attractionRepository;
        this.allGuests = new ArrayList<>();
        this.populateGuests();
    }

    private void populateGuests() throws IOException {

        List<Attraction> attractions = attractionRepository.getAllAttractions();

        Attraction attraction1 = attractions.get(0);
        Attraction attraction2 = attractions.get(1);
        Attraction attraction3 = attractions.get(2);
        Attraction attraction4 = attractions.get(3);
        Attraction attraction5 = attractions.get(4);
        Attraction attraction6 = attractions.get(5);
        Attraction attraction7 = attractions.get(6);
        Attraction attraction8 = attractions.get(7);

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
        this.add(g1); this.add(g2); this.add(g3); this.add(g4); this.add(g5);
        this.add(g6); this.add(g7); this.add(g8); this.add(g9); this.add(g10);
        attraction5.getInstructor().calculateSum();
        attraction8.getInstructor().calculateSum();

    }

    @Override
    public List<Guest> getAllGuests() {
        return this.allGuests;
    }

    @Override
    public void add(Guest guest) {
        for (Guest g: this.allGuests){
            if (g.getID().equals(guest.getID())){
                System.out.println("Besucher mit diesem ID schon existiert\n");
                return;
            }
        }
        this.allGuests.add(guest);
    }

    @Override
    public void delete(String id) {
        Guest guest = this.findByID(id);
        this.allGuests.remove(guest);
    }

    @Override
    public void update(Guest guest, String id) {
        Guest g = this.findByID(id);
        int position = this.allGuests.indexOf(g);
        this.allGuests.set(position,guest);
    }

    @Override
    public Guest findByID(String id) {
        for (Guest g: this.allGuests){
            if (g.getID().equals(id))
                return g;
        }
        return null;
    }
}
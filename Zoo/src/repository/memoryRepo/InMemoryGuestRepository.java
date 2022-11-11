package repository.memoryRepo;

import domain.Attraction;
import domain.Guest;
import repository.GuestRepository;
import repository.memoryRepo.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InMemoryGuestRepository implements GuestRepository {
    private List<Guest> allGuests;

    public InMemoryGuestRepository(List<Guest> allGuests) {
        this.allGuests = new ArrayList<>();
        this.populateGuests();
    }

    private void populateGuests(){
        LocalDate g1 = LocalDate.of(2002,2,1);
        LocalDate g2 = LocalDate.of(1997,2,1);
        LocalDate g3 = LocalDate.of(1967,3,1);
        LocalDate g4 = LocalDate.of(2002,4,11);
        LocalDate g5 = LocalDate.of(1989,8,12);
        LocalDate g6 = LocalDate.of(2009,2,1);
        LocalDate g7 = LocalDate.of(2010,7,16);
        LocalDate g8 = LocalDate.of(2021,11,18);
        LocalDate g9 = LocalDate.of(2018,3,3);
        LocalDate g10 = LocalDate.of(2001,5,28);
        LocalDate g11 = LocalDate.of(2000,3,23);
        LocalDate g12 = LocalDate.of(1970,12,24);
        LocalDate g13 = LocalDate.of(1956,8,8);
        LocalDate g14 = LocalDate.of(1999,7,7);
        LocalDate g15 = LocalDate.of(1999,11,12);
        LocalDate g16 = LocalDate.of(1988,2,1);
        LocalDate g17 = LocalDate.of(1995,3,16);
        LocalDate g18 = LocalDate.of(1988,9,29);
        Guest guest1 = new Guest("Kis", "Maria", g1,0.0, null);
        Guest guest2 = new Guest("Petru", "Ioana", g2, 0.0, null);
        Guest guest3 = new Guest("Comsa", "Ana", g3, 0.0, null);
        Guest guest4 = new Guest("Pop", "Otilia", g4, 0.0, null);
        Guest guest5 = new Guest("Ion", "Ionut", g5, 0.0, null);
        Guest guest6 = new Guest("Gal", "Timea", g6, 0.0, null);
        Guest guest7 = new Guest("Gal", "Emese", g7, 0.0, null);
        Guest guest8 = new Guest("Popa", "Ecaterina", g8, 0.0, null);
        Guest guest9 = new Guest("Katy", "Perry", g9, 0.0, null);
        Guest guest10 = new Guest("Selena", "Gomez", g10, 0.0, null);
        Guest guest11 = new Guest("Justin", "Bieber", g11, 0.0, null);
        Guest guest12 = new Guest("Céline", "Dion", g12, 0.0, null);
        Guest guest13 = new Guest("Leonardo", "DiCaprio", g13, 0.0, null);
        Guest guest14 = new Guest("Julia", "Roberts", g14, 0.0, null);
        Guest guest15 = new Guest("Tom", "Hanks", g15, 0.0, null);
        Guest guest16 = new Guest("Mel", "Gibson", g16, 0.0, null);
        Guest guest17 = new Guest("Jackie", "Chan", g17, 0.0, null);
        Guest guest18 = new Guest("Terence", "Hill", g18, 0.0, null);
        this.add(guest1);
        this.add(guest2);
        this.add(guest3);
        this.add(guest4);
        this.add(guest5);
        this.add(guest6);
        this.add(guest7);
        this.add(guest8);
        this.add(guest9);
        this.add(guest10);
        this.add(guest11);
        this.add(guest12);
        this.add(guest13);
        this.add(guest14);
        this.add(guest15);
        this.add(guest16);
        this.add(guest17);
        this.add(guest18);
    }

    @Override
    public List<Guest> getAllGuests() {
        return this.allGuests;
    }

    @Override
    public void add(Guest guest) {
        for (Guest g: this.allGuests){
            if (g.getID() == guest.getID()){
                System.out.println("Guest with this ID already exists");
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
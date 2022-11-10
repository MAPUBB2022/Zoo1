package repository.memoryRepo;

import domain.Guest;
import repository.GuestRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryGuestRepository implements GuestRepository {
    private List<Guest> allGuests;

    public InMemoryGuestRepository(List<Guest> allGuests) {
        this.allGuests = new ArrayList<>();
        this.populateGuests();
    }

    private void populateGuests(){
        /*
        Guest guest1 = new Guest("","","","","","");
        Guest guest2 = new Guest("","","","","","");
        Guest guest3 = new Guest("","","","","","");
        Guest guest4 = new Guest("","","","","","");
        Guest guest5 = new Guest("","","","","","");

        this.add(guest1);
        this.add(guest2);
        this.add(guest3);
        this.add(guest4);
        this.add(guest5);
        */
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
    public void update(String id, Guest guest) {
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

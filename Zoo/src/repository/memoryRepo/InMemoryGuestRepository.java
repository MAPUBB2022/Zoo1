package repository.memoryRepo;

import domain.Guest;
import repository.GuestRepository;

import java.util.List;

public class InMemoryGuestRepository implements GuestRepository {
    @Override
    public List<Guest> getAllGuests() {
        return null;
    }

    @Override
    public void add(Guest guest) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Guest guest, Integer integer) {

    }

    @Override
    public Guest findByID(Integer integer) {
        return null;
    }
}

package repository;
import domain.Guest;
import java.util.List;
public interface GuestRepository extends ICrudRepository<String, Guest>{
    List<Guest> getAllGuests();
    public void add(Guest guest);
    void delete(String id);
    void update(Guest guest, String id);
    Guest findByID(String id);
}

package repository;
import domain.Guest;
import java.util.List;
public interface GuestRepository extends ICrudRepository<Integer, Guest>{
    List<Guest> getAllGuests();
}

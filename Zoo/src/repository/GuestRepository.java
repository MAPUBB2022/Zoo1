package repository;

import domain.Guest;

import java.util.List;

public interface GuestRepository extends ICrudRepository <String, Guest>{
    List<Guest> getAllGuests();
}

package repository;

import domain.Attraction;

import java.util.List;

public interface AttractionRepository extends ICrudRepository<String, Attraction>{
    List<Attraction> getAllAttractions();
    void add(Attraction attraction);
    void delete(String ID);
    void update(Attraction attraction, String ID);
    Attraction findByID(String id);
}
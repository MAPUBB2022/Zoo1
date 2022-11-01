package repository;

import domain.Attraction;

import java.util.List;

public interface AttractionRepository extends ICrudRepository<Integer, Attraction>{
    List<Attraction> getAllAttractions();
}

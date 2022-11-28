package repository;

import domain.Attraction;

import java.util.List;

public interface AttractionRepository extends ICrudRepository<String, Attraction>{
    List<Attraction> getAllAttractions();
}

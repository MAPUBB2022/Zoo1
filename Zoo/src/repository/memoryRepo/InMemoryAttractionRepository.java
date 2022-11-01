package repository.memoryRepo;

import domain.Attraction;
import repository.AttractionRepository;

import java.util.List;

public class InMemoryAttractionRepository implements AttractionRepository {
    @Override
    public List<Attraction> getAllAttractions() {
        return null;
    }

    @Override
    public void add(Attraction attraction) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Attraction attraction, Integer integer) {

    }

    @Override
    public Attraction findByID(Integer integer) {
        return null;
    }
}

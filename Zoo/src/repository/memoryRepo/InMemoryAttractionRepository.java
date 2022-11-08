package repository.memoryRepo;

import domain.Attraction;
import repository.AttractionRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAttractionRepository implements AttractionRepository {

    private List<Attraction> allAttractions;

    public InMemoryAttractionRepository() {
        this.allAttractions = new ArrayList<>();
        this.populateAttractions();
    }

    private void populateAttractions(){
        /*
        Attraction attraction1 = new Attraction("",0,"","","","","");
        Attraction attraction2 = new Attraction("",0,"","","","","");
        Attraction attraction3 = new Attraction("",0,"","","","","");
        Attraction attraction4 = new Attraction("",0,"","","","","");
        Attraction attraction5 = new Attraction("",0,"","","","","");

        this.add(attraction1);
        this.add(attraction2);
        this.add(attraction3);
        this.add(attraction4);
        this.add(attraction5);
        */
    }

    @Override
    public List<Attraction> getAllAttractions() {
        return allAttractions;
    }

    @Override
    public void add(Attraction attraction) {
        for (Attraction attr: this.allAttractions){
            if (attr.getID() == attraction.getID()){
                System.out.println("Attraction with this ID already exists");
                return;
            }
        }
        this.allAttractions.add(attraction);
    }

    @Override
    public void delete(String id) {
        Attraction attraction = this.findbyId(id);
        this.allAttractions.remove(attraction);
    }

    @Override
    public void update(String id, Attraction attraction) {
        Attraction attr = this.findbyId(id);
        int position = this.allAttractions.indexOf(attr);
        this.allAttractions.set(position,attraction);
    }

    @Override
    public Attraction findbyId(String id) {
        for (Attraction attraction: this.allAttractions){
            if (attraction.getID().equals(id))
                return attraction;
        }
        return null;
    }
}

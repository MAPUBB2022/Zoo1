import registration.RegistrationSystem;
import repository.AttractionRepository;
import repository.GuestRepository;
import repository.InstructorRepository;
import repository.memoryRepo.InMemoryAttractionRepository;
import repository.memoryRepo.InMemoryGuestRepository;
import repository.memoryRepo.InMemoryInstructorRepository;
import ui.UI;

public class Main {
    public static void main(String[] args) {
        InstructorRepository instructorRepository = new InMemoryInstructorRepository();
        AttractionRepository attractionRepository = new InMemoryAttractionRepository(instructorRepository);
        GuestRepository guestRepository = new InMemoryGuestRepository(attractionRepository);

        RegistrationSystem controller = new RegistrationSystem(attractionRepository, guestRepository, instructorRepository);
        UI ui = new UI(controller);
        ui.getUserChoice();
    }
}
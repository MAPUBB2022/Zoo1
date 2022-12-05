import registration.RegistrationSystem;
import repository.AttractionRepository;
import repository.GuestRepository;
import repository.InstructorRepository;
import repository.JDBCrepository.JdbcAttractionRepository;
import repository.JDBCrepository.JdbcGuestRepository;
import repository.JDBCrepository.JdbcInstructorRepository;
import repository.memoryRepo.InMemoryAttractionRepository;
import repository.memoryRepo.InMemoryGuestRepository;
import repository.memoryRepo.InMemoryInstructorRepository;
import ui.UI;
import utils.NoMoreAvailableTicketsException;
import utils.NoSuchDataException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, NoMoreAvailableTicketsException, NoSuchDataException {
//        InstructorRepository instructorRepository = new InMemoryInstructorRepository();
//        AttractionRepository attractionRepository = new InMemoryAttractionRepository(instructorRepository);
//        GuestRepository guestRepository = new InMemoryGuestRepository(attractionRepository);
//
//        RegistrationSystem controller = new RegistrationSystem(attractionRepository, guestRepository, instructorRepository);
//        UI ui = new UI(controller);
//        ui.getUserChoice();

        JdbcInstructorRepository jdbcInstructorRepository = new JdbcInstructorRepository("default","jdbc:postgresql://localhost:5432/Zoo","postgres","timi3678");
        JdbcAttractionRepository jdbcAttractionRepository = new JdbcAttractionRepository(jdbcInstructorRepository);
        GuestRepository jdbcGuestRepository = new JdbcGuestRepository(jdbcAttractionRepository);

        RegistrationSystem jdbcController = new RegistrationSystem(jdbcAttractionRepository, jdbcGuestRepository, jdbcInstructorRepository);
        UI jdbcUi = new UI(jdbcController);
        jdbcUi.getUserChoice();

    }
}



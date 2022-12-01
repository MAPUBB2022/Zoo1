public class Main {
    public static void main(String[] args) throws IOException, NoMoreAvailableTicketsException, NoSuchDataException {
//        InstructorRepository instructorRepository = new InMemoryInstructorRepository();
//        AttractionRepository attractionRepository = new InMemoryAttractionRepository(instructorRepository);
//        GuestRepository guestRepository = new InMemoryGuestRepository(attractionRepository);
//
//        RegistrationSystem controller = new RegistrationSystem(attractionRepository, guestRepository, instructorRepository);
//        UI ui = new UI(controller);
//        ui.getUserChoice();

        InstructorRepository instructorRepository = new JSONInstructorRepository("src/main/resources/instructors.json");
        AttractionRepository attractionRepository = new JSONAttractionRepository("src/main/resources/attractions.json", instructorRepository);
        GuestRepository guestRepository = new JSONGuestRepository("src/main/resources/guests.json", attractionRepository);

        RegistrationSystem controller = new RegistrationSystem(attractionRepository, guestRepository, instructorRepository);
        UI ui = new UI(controller);
        ui.getUserChoice();
    }
}

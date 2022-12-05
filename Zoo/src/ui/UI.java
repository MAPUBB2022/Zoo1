package ui;

import domain.Attraction;
import domain.Guest;
import domain.Instructor;
import domain.Weekday;
import registration.RegistrationSystem;
import repository.AttractionRepository;
import repository.GuestRepository;
import repository.InstructorRepository;
import repository.memoryRepo.InMemoryAttractionRepository;
import repository.memoryRepo.InMemoryGuestRepository;
import repository.memoryRepo.InMemoryInstructorRepository;
import utils.NoMoreAvailableTicketsException;
import utils.NoSuchDataException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UI {
    private RegistrationSystem controller;

    public UI(RegistrationSystem controller) {
        this.controller = controller;
    }

    public void showMenuRegistration() {
        System.out.println("""
                1. Registration
                2. Schon registriert
                """);
    }
    public void showMenuGuestRegistration() {
        System.out.println("""
                1. Registration
                2. Schon registriert
                """);
    }

    public void showMenuGuest(){
        System.out.println(""+
                "1. Zeige alle Attraktionen \n" +
                "2. Zeige alle verfügbare Attraktionen\n" +
                "3. Zeige Attraktionen nach einem bestimmten Tag \n" +
                "4. Für eine Attraktion anmelden\n" +
                "5. Attraktionen, bei denen Sie angemeldet sind\n" +
                "6. Zeige die bezahlende Endsumme\n" +
                "7. Zeige Attraktionen sortiert nach Preis\n" +
                "8. Zeige Attraktionen die unter eine spezifische Preis sind\n" +
                "9. Exit\n");
    }

    public void showMenuInstructor(){
        System.out.println("" +
                "1. Registration\n" +
                "2. Zeige gehältende Attraktionen\n" +
                "3. Neue Attraktion einfügen\n" +
                "4. Attraktion absagen\n" +
                "5. Exit\n");
    }

    public void showMenuManager(){
        System.out.println("" +
                "1. Zeige alle Attraktionen \n" +
                "2. Zeige alle Instruktoren \n" +
                "3. Zeige alle Attraktionen sortiert nach BesucherAnzahl\n" +
                "4. Zeige alle Instruktoren die größeres Gehalt bekommen, als der Durchschnitt\n" +
                "5. Exit \n");
    }
    // menu Instructor - Guest - Manager
    public void showMenuIGM(){
        System.out.println("""
                1. Instruktor
                2. Besucher
                3. Manager
                """);
    }

    public void getUserChoice() throws IOException, NoMoreAvailableTicketsException, NoSuchDataException {
        System.out.println("Wähle eine Option: ");
        this.showMenuIGM();
        Scanner in = new Scanner(System.in);
        int choice = in.nextInt(), choiceMenu = 0;
        boolean successful;
        String idGuest = null, idAttraction = null, idInstructor, emptyLine, username = null, password;
        while (choice == 1 || choice == 2 || choice == 3) {
            if (choice == 1) {
                successful = false;
                while (!successful) {
                    showMenuRegistration();
                    choiceMenu = in.nextInt();
                    switch (choiceMenu) {
                        case 1:
                            // registration
                            Instructor instructor = readInInstructor();
                            username = instructor.getID();
                            successful = this.controller.addInstructor(instructor);
                            if (successful) {
                                System.out.println("Registration erfolgreich!\n");
                            } else
                                System.out.println("Registration nicht möglich!\n");
                            break;
                        case 2:
                            // authentification
                            System.out.print("Gib deinen Username an: ");
                            emptyLine = in.nextLine();
                            username = in.nextLine();
                            System.out.print("Gib dein Passwort an: ");
                            password = in.nextLine();
                            Instructor instr = this.controller.findInstructorByUsername(username);
                            if (instr != null) {
                                while (!instr.matchesPassword(password)) {
                                    System.out.println("Falsches Passwort, versuche es wieder: ");
                                    password = in.nextLine();
                                }
                                successful = true;
                                System.out.println("Das Passwort passt!");
                            } else
                                System.out.println("Es gibt keine Instruktor mit diesem Username");
                            break;
                        default:
                            System.out.println("Es gibt so eine Option nicht");
                            break;
                    }
                }
                showMenuInstructor();
                choiceMenu = in.nextInt();
                while (choiceMenu != 5) {
                    switch (choiceMenu) {
                        case 1:
                            Instructor instructor = this.controller.findInstructorByUsername(username);
                            System.out.println(instructor.getAttractions());
                            break;
                        case 2:
                            Attraction attraction = readInAttraction();
                            this.controller.addAttraction(attraction, username);
                            break;
                        case 3:
                            instructor = this.controller.findInstructorByUsername(username);
                            System.out.println(instructor.getAttractions());
                            System.out.println("Gib ID-Attraktion an");
                            emptyLine = in.nextLine();
                            idAttraction = in.nextLine();
                            successful = this.controller.deleteAttraction(username, idAttraction);
                            if (successful)
                                System.out.println("Attraktion ist abgesagt");
                            else
                                System.out.println("Prozess fehlgeschlafen");
                            break;
                        case 4:
                            System.out.println("Summe: " + this.controller.getSumFromGuests(username));
                            break;
                        case 5:
                            break;
                        default:
                            System.out.println("So eine Option exisitiert nicht!\n");
                    }
                    showMenuInstructor();
                    choiceMenu = in.nextInt();
                }
            }
            else if (choice == 2) {
                successful = false;
                while (!successful) {
                    showMenuRegistration();
                    choiceMenu = in.nextInt();
                    switch (choiceMenu) {
                        case 1:
                            // registration
                            Guest guest = readInGuest();
                            username = guest.getID();
                            successful = this.controller.addGuest(guest);
                            if (successful) {
                                System.out.println("Registration erfolgreich!\n");
                            } else
                                System.out.println("Registration nicht möglich!\n");
                            break;
                        case 2:
                            // authentification
                            System.out.print("Gib deinen Username an: ");
                            emptyLine = in.nextLine();
                            username = in.nextLine();
                            System.out.print("Gib dein Passwort an: ");
                            password = in.nextLine();
                            Guest g = this.controller.findGuestByUsername(username);
                            if (g != null) {
                                while (!g.matchesPassword(password)) {
                                    System.out.println("Falsches Passwort, versuche es wieder: ");
                                    password = in.nextLine();
                                }
                                successful = true;
                                System.out.println("Das Passwort passt!");
                            } else
                                System.out.println("Es gibt keine Benutzer mit diesem Username");
                            break;
                        default:
                            System.out.println("Es gibt so eine Option nicht");
                            break;
                    }
                }
                showMenuGuest();
                choiceMenu = in.nextInt();
                while (choiceMenu != 10) {
                    switch (choiceMenu) {
                        case 1:
                            System.out.println(this.controller.getAttractionsSortedByTitle());
                            break;
                        case 2:
                            System.out.println(this.controller.getAllAttractionsWithFreePlaces());
                            break;
                        case 3:
                            System.out.println("Gib einen Tag an: ");
                            emptyLine = in.nextLine();
                            Weekday day = Weekday.valueOf(in.nextLine().toUpperCase());
                            System.out.println(this.controller.getAttractionsAfterAGivenDay(day));
                            break;
                        case 4:
                            System.out.println(this.controller.getAttractionsSortedByTitle());
                            System.out.println("Gib eine Attraktion ID an: ");
                            emptyLine = in.nextLine();
                            idAttraction = in.nextLine();
                            successful = this.controller.signUpForAttraction(username, idAttraction);
                            if (successful)
                                System.out.println("Anmeldung erfolgreich!\n");
                            else
                                System.out.println("Anmeldung nicht möglich!\n");
                            break;
                        case 5:
                            System.out.println(this.controller.getAttractionsOfGuest(username));
                            break;
                        case 6:
                            System.out.println(this.controller.getFinalSumOfGuest(username));
                            break;
                        case 8:
                            System.out.println("Gib den Preis an: ");
                            double preis = in.nextDouble();
                            System.out.println(this.controller.filterAttractionsByAGivenValue(preis));
                            break;
                        default:
                            System.out.println("So eine Option exisitiert nicht!\n");
                    }
                    showMenuGuest();
                    choiceMenu = in.nextInt();
                }
            } else {
                showMenuManager();

                //                "1. Zeige alle Attraktionen \n" +
                //                "2. Zeige alle Instruktoren \n" +
                //                "3. Zeige alle Attraktionen sortiert nach BesucherAnzahl\n" +
                //                "4. Zeige alle Instruktoren die größeres Gehalt bekommen, als der Durchschnitt\n" +
                //                "5. Exit \n"
                choiceMenu = in.nextInt();
                while (choiceMenu != 7) {
                    switch (choiceMenu) {
                        case 1:
                            System.out.println(this.controller.getAllAttractions());
                            break;
                        case 2:
                            System.out.println(this.controller.getAllInstructors());
                            break;
                        case 3:
                            ///
                            break;
                        case 4:
                            System.out.println(this.controller.filterInstructorsWithHigherSalaryThanTheAverage());
                            break;
                        case 5:
                            System.out.println(this.controller.getAllAttractions());
                            System.out.println("Wähle eine Attraktion-ID: ");
                            emptyLine = in.nextLine();
                            idAttraction = in.nextLine();
                            System.out.println("ID: " + idAttraction);
                            List<Guest> guests = this.controller.getGuestsOfAttraction(idAttraction);
                            if (guests != null)
                                System.out.println(guests);
                            else
                                System.out.println("Es gibt noch keine angemeldete Besuchern");
                            break;
                        case 6:
                            System.out.println(this.controller.getAllAttractions());
                            System.out.println("Wähle eine Attraktion-ID: ");
                            emptyLine = in.nextLine();
                            idAttraction = in.nextLine();
                            System.out.println("Wähle einen neuen Instruktor: ");
                            System.out.println(this.controller.getAllInstructors());
                            System.out.println("Gib ID an: ");
                            idInstructor = in.nextLine();
                            successful = this.controller.changeInstructorOfAttraction(idAttraction,idInstructor);
                            if (successful) {
                                System.out.println("Veränderungen gespeichert!\n");
                            } else
                                System.out.println("Prozess fehlgeschlagen\n");
                            break;
                        case 7:
                            break;
                        default:
                            System.out.println("Es gibt so eine Option nicht");
                            break;
                    }
                    showMenuManager();
                    choiceMenu = in.nextInt();
                }
            }
            System.out.println("Wähle eine Option: ");
            showMenuIGM();
            choice = in.nextInt();
        }
    }

    public Guest readInGuest(){
        Scanner in = new Scanner(System.in);
        System.out.println("Gib deinen Username an:");
        String username = in.nextLine();
        System.out.println("Gib deinen Vorname an:");
        String firstName = in.nextLine();
        System.out.println("Gib deinen Nachname an:");
        String lastName = in.nextLine();
        System.out.println("Gib dein Geburtsdatum an:");
        LocalDate birthday = LocalDate.parse(in.nextLine());
        System.out.println("Gib dein Passwort an:");
        String password = in.nextLine();
        return new Guest(username, firstName, lastName, password, birthday);
    }

    public Instructor readInInstructor() {
        Scanner in = new Scanner(System.in);
        System.out.println("Gib deinen Username an:");
        String username = in.nextLine();
        System.out.println("Gib deinen Vorname an:");
        String firstName = in.nextLine();
        System.out.println("Gib deinen Nachname an:");
        String lastName = in.nextLine();
        System.out.println("Gib dein Passwort an:");
        String password = in.nextLine();
        return new Instructor(username, firstName, lastName, password);
    }

    public Attraction readInAttraction(){
        Scanner in = new Scanner(System.in);
        System.out.println("Gib mir der Name der Attraktion: \n");
        String name = in.nextLine();
        System.out.println("Gib mir die Kapazitaet des Ortes für diese Attraktion: \n");
        int capacity = in.nextInt();
        System.out.println("Gib mir den Preis dieser Attraktion: \n");
        double price = in.nextDouble();
        System.out.println("Gib mir die Stätte: \n");
        String empty = in.nextLine();
        String location = in.nextLine();
        System.out.println("Gib mir den Tag: \n");
        Weekday weekday = Weekday.valueOf(in.nextLine().toUpperCase());
        return  new Attraction(name, capacity, null, price, location, weekday);
    }

    public void showGuestData(List<Guest> guests){
        for (Guest g: guests){
            System.out.println(g.getData());
        }

    }
}
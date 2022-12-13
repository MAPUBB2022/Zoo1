package ui;

import domain.Attraction;
import domain.Guest;
import domain.Instructor;
import domain.Weekday;
import registration.RegistrationSystem;
import utils.NoMoreAvailableTicketsException;
import utils.NoSuchDataException;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UI {
    private final RegistrationSystem controller;

    public UI(RegistrationSystem controller) {
        this.controller = controller;
    }

    public void showMenuRegistration() {
        System.out.println("""
                
                1. Registration
                2. Schon registriert
                """);
    }

    public void showMenuGuest(){
        System.out.println("""
                
                1. Zeige alle Attraktionen
                2. Zeige alle verfügbare Attraktionen
                3. Zeigen Attraktionen nach einem bestimmten Tag
                4. Für eine Attraktion anmelden
                5. Attraktionen, für denen Sie angemeldet sind
                6. Zeige die bezahlende Endsumme
                7. Zeige Attraktionen nach Preis sortiert
                8. Zeige Attraktionen mit einem kleinern Preis als ein gegebener Wert
                9. Exit
                """);
    }

    public void showMenuInstructor(){
        System.out.println("""
                
                1. Zeige gehältende Attraktionen
                2. Neue Attraktion einfügen
                3. Attraktion absagen
                4. Zeige Summe von Besucher
                5. Exit
                """);
    }

    public void showMenuManager(){
        System.out.println("""
                
                1. Zeige alle Attraktionen
                2. Zeige alle Instruktoren
                3. Zeige alle Besucher
                4. Zeige alle Besucher absteigend sortiert nach der bezahlten Gesamtsumme
                5. Zeige Besucher einer Attraktion
                6. neuen Instruktor für eine Attraktion auswählen
                7. Attraktionen nach BesucherAnzahl sortiert
                8. Zeige Instruktoren mit höherem Einkommen als der Durchschnitt
                9. Zeige durschnittliche Einkommen der Instruktoren
                10. Exit
                """);
    }

    // menu Instructor - Guest - Manager
    public void showMenuIGM(){
        System.out.println("""
                
                1. Instruktor
                2. Besucher
                3. Manager
                """);
    }

    public void getUserChoice(){
        System.out.println("Wähle eine Option: ");
        this.showMenuIGM();
        Scanner in = new Scanner(System.in);
        int choice = in.nextInt(), choiceMenu = 0;
        boolean successful;
        double value;
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
                            successful = this.controller.addInstructor(instructor);
                            if (successful) {
                                System.out.println("Registration erfolgreich!\n");
                                username = instructor.getID();
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
                                    System.out.print("Falsches Passwort, versuche es wieder: ");
                                    password = in.nextLine();
                                }
                                successful = true;
                                System.out.println("Das Passwort passt!");
                            } else
                                System.out.println("Es gibt keine Instruktor mit diesem Benutzername");
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
                            System.out.println(this.controller.getAttractionsOfInstructor(username));
                            break;
                        case 2:
                            Attraction attraction = readInAttraction();
                            successful = this.controller.addAttraction(attraction, username);
                            if (successful)
                                System.out.println("Attraktion eingefügt");
                            else
                                System.out.println("Attraktion ist nicht eingefügt");
                            break;
                        case 3:
                            System.out.println(this.controller.getAttractionsOfInstructor(username));
                            System.out.println("Gib ID-Attraktion an: ");
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
                                successful = this.controller.addGuest(guest);
                                if (successful) {
                                    System.out.println("Registration erfolgreich!\n");
                                    username = guest.getID();
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
                                        System.out.print("Falsches Passwort, versuche es wieder: ");
                                        password = in.nextLine();
                                    }
                                    successful = true;
                                    System.out.println("Das Passwort passt!");
                                } else
                                    System.out.println("Es gibt keinen Benutzer mit diesem Username");
                                break;
                            default:
                                System.out.println("Es gibt so eine Option nicht");
                                break;
                        }
                    }
                    showMenuGuest();
                    choiceMenu = in.nextInt();
                    while (choiceMenu != 9) {
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
                                Weekday day = this.controller.verifiedUserInputWeekday(in.nextLine());
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
                            case 7:
                                System.out.println(this.controller.getAttractionsSortedByPriceAscending());
                            case 8:
                                System.out.print("Gib den maximum Preis: ");
                                emptyLine = in.nextLine();
                                value = this.controller.verifiedUserInputPrice(in.nextLine());
                                System.out.println(this.controller.filterAttractionsByAGivenValue(value));
                                break;
                            case 9:
                                break;
                            default:
                                System.out.println("So eine Option exisitiert nicht!\n");
                        }
                        showMenuGuest();
                        choiceMenu = in.nextInt();
                    }
                } else {
                    showMenuManager();
                    choiceMenu = in.nextInt();
                    while (choiceMenu != 10) {
                        switch (choiceMenu) {
                            case 1:
                                System.out.println(this.controller.getAllAttractions());
                                break;
                            case 2:
                                System.out.println(this.controller.getAllInstructors());
                                break;
                            case 3:
                                this.showGuestData(this.controller.getAllGuests());
                                break;
                            case 4:
                                System.out.println(this.controller.getGuestsSortedDescendingBySum());
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
                                System.out.println(this.controller.getAttractionsSortedByGuestAscending());
                                break;
                            case 8:
                                System.out.println(this.controller.filterInstructorsWithHigherSalaryThanAverage());
                                break;
                            case 9:
                                System.out.println(this.controller.getAverageSalaryOfInstructors());
                                break;
                            case 10:
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
        System.out.print("Gib deinen Username an: ");
        String username = in.nextLine();
        System.out.print("Gib deinen Vorname an: ");
        String firstName = in.nextLine();
        System.out.print("Gib deinen Nachname an: ");
        String lastName = in.nextLine();
        System.out.print("Gib dein Geburtsdatum an: ");
        String birthday = in.nextLine();
        System.out.print("Gib dein Passwort an: ");
        String password = in.nextLine();
        Guest guest = this.controller.verifiedUserInputGuest(username, firstName, lastName, birthday, password);
        return guest;
    }

    public Instructor readInInstructor() {
        Scanner in = new Scanner(System.in);
        System.out.print("Gib deinen Username an: ");
        String username = in.nextLine();
        System.out.print("Gib deinen Vorname an: ");
        String firstName = in.nextLine();
        System.out.print("Gib deinen Nachname an: ");
        String lastName = in.nextLine();
        System.out.print("Gib dein Passwort an: ");
        String password = in.nextLine();
        boolean correctInput = this.controller.verifyUserInputNameAndPassword(username,firstName,lastName,password);
        if (correctInput)
            return new Instructor(username, firstName, lastName, password);
        return null;
    }

    public Attraction readInAttraction() {
        Scanner in = new Scanner(System.in);
        System.out.print("Gib der Name der Attraktion an: ");
        String name = in.nextLine();
        System.out.print("Gib die Kapazitaet des Ortes für diese Attraktion an: ");
        String capacity = in.nextLine();
        System.out.print("Gib den Preis dieser Attraktion an: ");
        String price = in.nextLine();
        System.out.print("Gib die Stätte an: ");
        String location = in.nextLine();
        System.out.print("Gib den Tag an: ");
        String weekday = in.nextLine();
        return this.controller.verifiedUserInputAttraction(name, capacity, price, location, weekday);
    }

    public void showGuestData(List<Guest> guests){
        for (Guest g: guests){
            System.out.println(g.getData());
        }

    }
}

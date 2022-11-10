package ui;

import domain.Attraction;
import domain.Guest;
import domain.Weekday;
import registration.RegistrationSystem;

import java.time.LocalDate;
import java.util.Scanner;

public class UI {
    private RegistrationSystem controller;

    public UI(RegistrationSystem controller) {
        this.controller = controller;
    }

    public void showMenuGuestRegistration() {
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
                7. Exit
                """);
    }

    public void showMenuInstructor(){
        System.out.println("""
                1. Registration
                2. Zeige gehältende Attraktionen
                3. Neue Attraktion einfügen
                4. Attraktion absagen
                5. Exit
                """);
    }

    public void showMenuManager(){
        System.out.println("""
                1. Zeige alle Attraktionen
                2. Zeige alle Instruktoren
                3. Exit
                """);
    }

    public void getUserChoice(){
        System.out.println("Wähle eine Option");
        System.out.println("1. Instruktor");
        System.out.println("2. Besucher");
        System.out.println("3. Manager");
        Scanner in = new Scanner(System.in);
        Integer choice =  in.nextInt(), choiceMenu;
        boolean successful;
        String idGuest = null, idAttraction;
        while (choice == 1 || choice == 2 || choice == 3){
            if (choice == 1){
                showMenuInstructor();
            }
            else if (choice == 2){
                choiceMenu = 0;
                while (choiceMenu != 1 && choiceMenu !=2) {
                    showMenuGuestRegistration();
                    choiceMenu = in.nextInt();
                    switch (choice){
                        case 1:
                            Guest guest = readInGuest();
                            successful = this.controller.addGuest(guest);
                            if (successful) {
                                System.out.println("Registration erfolgreich!\n");
                                idGuest = guest.getID();
                                System.out.println("Deine ID ist: " + idGuest + "\n");
                            } else
                                System.out.println("Registration nicht möglich!\n");
                            break;
                        case 2:
                            System.out.println("Gib deine ID an");
                            idGuest = in.nextLine();
                        default:
                            System.out.println("Es gibt so eine Option nicht");
                    }
                }
                while (choiceMenu != 8){
                    showMenuGuest();
                    switch (choiceMenu) {
                        case 1:
                            System.out.println(this.controller.getAttractionsSortedByTitle());
                            break;
                        case 2:
                            System.out.println(this.controller.getAllAttractionsWithFreePlaces());
                            break;
                        case 3:
                            System.out.println("Gib einen Tag an: ");
                            Weekday day = Weekday.valueOf(in.nextLine());
                            System.out.println(this.controller.getAttractionsAfterAGivenDay(day));
                            break;
                        case 4:
                            System.out.println(this.controller.getAttractionsSortedByTitle());
                            System.out.println("Gib eine Attraktion ID an: ");
                            idAttraction = in.nextLine();
                            successful = this.controller.signUpForAttraction(idGuest, idAttraction, LocalDate.now());
                            if (successful)
                                System.out.println("Anmeldung erfolgreich!\n");
                            else
                                System.out.println("Anmeldung nicht möglich!\n");
                            break;
                        case 5:
                            System.out.println(this.controller.getAttractionsOfGuest(idGuest));
                            break;
                        case 6:
                            System.out.println(this.controller.getFinalSumOfGuest(idGuest));
                            break;
                        case 7:
                            break;
                        default:
                            System.out.println("So eine Option exisitiert nicht!\n");
                    }
                    showMenuGuest();
                    choiceMenu = in.nextInt();
                }
            }
            else {
                showMenuManager();
            }
            System.out.println("Wähle eine Option");
            System.out.println("1. Instruktor");
            System.out.println("2. Besucher");
            System.out.println("3. Manager");
            choice = in.nextInt();
        }
    }

    public Guest readInGuest(){
        Scanner in = new Scanner(System.in);
        System.out.println("Gib deinen Vorname an");
        String firstName = in.nextLine();
        System.out.println("Gib deinen Nachname an");
        String lastName = in.nextLine();
        System.out.println("Gib dein Geburtsdatum an");
        LocalDate birthday = LocalDate.parse(in.nextLine());
        return new Guest(firstName,lastName,birthday);
    }
}

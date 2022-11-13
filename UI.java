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

import java.util.List;
import java.util.Scanner;

public class UI {
    private RegistrationSystem controller;

    public UI(RegistrationSystem controller) {
        this.controller = controller;
    }

    public void showMenuGuest(){
        System.out.println("" +
                "1. Registration \n" +
                "2. Zeige alle Attraktionen \n" +
                "3. Zeige alle verfügbare Attraktionen\n" +
                "4. Zeige Attraktionen nach einem bestimmten Tag \n" +
                "5. Für eine Attraktion anmelden\n" +
                "6. Attraktionen, bei denen Sie angemeldet sind\n" +
                "7. Zeige die bezahlende Endsumme\n" +
                "8. Exit\n");
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
                "3. Exit \n");
    }

    public void getUserChoice(){
        System.out.println("Wähle eine Option: ");
        System.out.println("1. Instruktor ");
        System.out.println("2. Besucher ");
        System.out.println("3. Manager ");
        Integer choice;
        Scanner in = new Scanner(System.in);
        choice = in.nextInt();
        while(choice ==1 || choice == 2 || choice == 3){
            if(choice == 1){
                showMenuGuest();
            }
            else if(choice == 2){
                showMenuInstructor();
                Integer choose;
                Scanner in_ = new Scanner(System.in);
                choose = in_.nextInt();
                if(choose == 1) {
                    System.out.println("Gib mir dein Vorname: \n");
                    Scanner firstName_ = new Scanner(System.in);
                    String firstName = firstName_.nextLine();
                    System.out.println("Gib mir dein Nachname: \n");
                    Scanner lastName_ = new Scanner(System.in);
                    String lastName = lastName_.nextLine();
                    boolean found = false;
                    for(Instructor i: this.controller.getAllInstructors()){
                        if(i.getFirstName() == firstName && i.getLastName() == lastName) {
                            System.out.println("Du bist schon ein Instruktor! \n");
                            found  = true;
                        }

                    }
                    if(found == false) {
                        Instructor instructor = new Instructor(null,firstName, lastName);
                        this.controller.getAllInstructors().add(instructor);
                    }
                }
                else if(choose == 2){
                    System.out.println("Gib mir dein Vorname: \n");
                    Scanner firstName_ = new Scanner(System.in);
                    String firstName = firstName_.nextLine();

                    System.out.println("Gib mir dein Nachname: \n");
                    Scanner lastName_ = new Scanner(System.in);
                    String lastName = lastName_.nextLine();

                    for(Instructor i: this.controller.getAllInstructors()){
                        if(i.getFirstName() == firstName && i.getLastName() == lastName) {
                            System.out.println("Gib mir dein ID: ");
                            Scanner id_ = new Scanner(System.in);
                            String id = id_.nextLine();

                            if(id == i.getID()) {
                                for(Attraction a: i.attractionOfInstructor){
                                    System.out.println(a.toString());
                                }
                            }
                            else System.out.println("Inkorrekt ID oder noch nicht im System! \n");
                        }
                    }
                }
                else if(choose == 3) {
                    System.out.println("Gib mir dein ID: ");
                    Scanner id_ = new Scanner(System.in);
                    String id = id_.nextLine();
                    boolean found = false;
                    for(Instructor i: this.controller.getAllInstructors()){
                        if(i.getID() == id){
                            found = true;
                            System.out.println("Gib mir der Name der Attraktion: \n");
                            Scanner name_ = new Scanner(System.in);
                            String name = name_.nextLine();

                            System.out.println("Gib mir die Kapazitaet des Ortes für diese Attraktion: \n");
                            Scanner capacity_ = new Scanner(System.in);
                            Integer capacity = capacity_.nextInt();
                            capacity_.nextLine();

                            System.out.println("Gib mir den Preis dieser Attraktion: \n");
                            Scanner price_ = new Scanner(System.in);
                            double price = price_.nextDouble();
                            price_.nextLine();

                            System.out.println("Gib mir die Stätte: \n");
                            Scanner location_ = new Scanner(System.in);
                            String location = location_.nextLine();

                            System.out.println("Gib mir den Tag: \n");
                            Scanner weekday_ = new Scanner(System.in);
                            Weekday weekday = Weekday.valueOf(weekday_.next().toUpperCase());

                            Attraction attraction = new Attraction(name, capacity, i, null, price, location, weekday);

                            i.attractionOfInstructor.add(attraction);
                        }
                    }
                    if(found == false) System.out.println("Du bist nicht ein Instruktor, du kannst nichst machen! \n");
                }
                else if(choose == 4){
                    System.out.println("Gib mir dein Vorname: \n");
                    Scanner firstName_ = new Scanner(System.in);
                    String firstName = firstName_.nextLine();

                    System.out.println("Gib mir dein Nachname: \n");
                    Scanner lastName_ = new Scanner(System.in);
                    String lastName = lastName_.nextLine();

                    for(Instructor i: this.controller.getAllInstructors()){
                        if(i.getFirstName() == firstName && i.getLastName() == lastName) {
                            System.out.println("Gib mir dein ID: ");
                            Scanner id_ = new Scanner(System.in);
                            String id = id_.nextLine();

                            if(id == i.getID()) {
                                System.out.println("Welche Attraktion möchtest du absagen? \n");
                                Scanner idAttraction_ = new Scanner(System.in);
                                String idAttraction = id_.nextLine();
                                for(Attraction a: i.attractionOfInstructor){
                                    if(a.getID() == idAttraction){
                                        this.controller.deleteAttraction(id,idAttraction);
                                    }
                                }
                            }
                            else System.out.println("Inkorrekt ID oder noch nicht im System! \n");
                        }
                    }
                }
                else if(choose == 5){
                    showMenuInstructor();
                }
            }
            else if(choice == 3){
                showMenuManager();

                Integer choose;
                Scanner in_ = new Scanner(System.in);
                choose = in_.nextInt();
                if(choose == 1){
                    this.controller.getAllAttractions();
                }
                else if(choose == 2){
                    this.controller.getAllInstructors();
                }
                else if(choose == 3){
                    showMenuManager();
                }
            }
            System.out.println("Wähle eine Option: ");
            System.out.println("1. Instruktor ");
            System.out.println("2. Besucher ");
            System.out.println("3. Manager ");
            choice = in.nextInt();
        }
    }
}

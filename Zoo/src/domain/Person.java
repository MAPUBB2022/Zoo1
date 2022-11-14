package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

abstract class Person {
    final protected String ID;
    protected double finalSum;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected List<Attraction> attractions;

    public Person(String id, String firstName, String lastName, String password, List<Attraction> attractions, double sum) {
        if ((firstName+lastName).matches(".*[@#$%^&*0-9].*"))
            throw new IllegalArgumentException();
        this.firstName = firstName;
        this.lastName = lastName;
        this.attractions = attractions;
        this.finalSum = sum;
        this.ID = id;
        this.password = password;
    }

    public Person(String id, String firstName, String lastName, String password) {
        if ((firstName+lastName).matches(".*[@#$%^&*0-9].*"))
            throw new IllegalArgumentException();
        this.firstName = firstName;
        this.lastName = lastName;
        this.attractions = new ArrayList<Attraction>();
        this.finalSum = 0;
        this.ID = id;
        this.password = password;
    }
    abstract String getData();

    public abstract void calculateSum();

    public String getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName(){
        return this.firstName + ' ' + this.lastName;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    public double getFinalSum() {
        return finalSum;
    }

    public void setFinalSum(double sum){
        finalSum = sum;
    }

    public void addAttraction(Attraction attraction){
        this.attractions.add(attraction);
        calculateSum();
    }

    public void removeAttraction(Attraction attraction){
        this.attractions.remove(attraction);
        calculateSum();
    }

    @Override
    public String toString() {
        return "Person{" +
                "ID='" + ID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", attractions=" + attractions +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public boolean matchesPassword(String password){
        return this.password.equals(password);
    }
}

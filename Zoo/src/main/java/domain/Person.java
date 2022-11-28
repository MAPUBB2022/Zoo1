package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@MappedSuperclass
abstract class Person {
    @Id
    protected String ID;
    protected double finalSum;
    protected String password;
    protected String firstName;
    protected String lastName;

    public Person(String id, String firstName, String lastName, String password, double sum) {
        if ((firstName+lastName).matches(".*[@#$%^&*0-9].*"))
            throw new IllegalArgumentException();
        this.firstName = firstName;
        this.lastName = lastName;
        this.finalSum = sum;
        this.ID = id;
        this.password = password;
    }

    public Person(String id, String firstName, String lastName, String password) {
        if ((firstName+lastName).matches(".*[@#$%^&*0-9].*"))
            throw new IllegalArgumentException();
        this.firstName = firstName;
        this.lastName = lastName;
        this.finalSum = 0;
        this.ID = id;
        this.password = password;
    }

    public Person(){}

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


    public double getFinalSum() {
        return finalSum;
    }

    public void setFinalSum(double sum){
        finalSum = sum;
    }

    @Override
    public String toString() {
        return "Person{" +
                "ID='" + ID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean matchesPassword(String password){
        return this.password.equals(password);
    }
}

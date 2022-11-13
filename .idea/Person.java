package domain;

import java.util.UUID;

abstract class Person {
    protected String firstName;
    protected String lastName;
    final protected String ID;
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = this.firstName.substring(0,1)+this.lastName.substring(0,1)+this.firstName.length();
    }
    abstract String getData();

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getID() {
        return ID;
    }

    public String getName(){
        return this.firstName + ' ' + this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

package domain;

import java.util.UUID;

abstract class Person {
    final protected String ID;
    protected String firstName;
    protected String lastName;
    public Person(String firstName, String lastName) {
        if ((firstName+lastName).matches(".*[@#$%^&*0-9].*"))
            throw new IllegalArgumentException();
        this.firstName = firstName;
        this.lastName = lastName;
        // create ID
        this.ID = UUID.randomUUID().toString();;
    }
    abstract String getData();

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
}

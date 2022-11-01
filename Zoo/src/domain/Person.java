package domain;
abstract class Person {
    protected String firstName;
    protected String lastName;
    final protected Integer ID;
    public Person(String firstName, String lastName, Integer ID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID;
    }
    abstract String getData();

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getID() {
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

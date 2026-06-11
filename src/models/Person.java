package models;

public abstract class Person {
    protected String name;
    protected String phone;

    public Person(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
    // Добавь это внутрь класса Person.java:
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getName() { return name; }
    public String getPhone() { return phone; }
}
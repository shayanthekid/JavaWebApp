/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author sajid
 */
public class Employee {
    private int id;
    private String name;
    private int contact;

    private Employee(EmployeeBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.contact = builder.contact;
    }

    public static class EmployeeBuilder {

        private int id;
        private String name;
        private int contact;

        public EmployeeBuilder(int id) {
            this.id = id;
        }

        public EmployeeBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public EmployeeBuilder setContact(int contact) {
            this.contact = contact;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    // getters and setters for the properties
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }
}

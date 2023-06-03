/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author sajid
 */
public class Inventory {

    private int id;
    private String name;
    private float price;
    private int quantity;

    private Inventory(InventoryBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.quantity = builder.quantity;
    }

    public static class InventoryBuilder {

        private int id;
        private String name;
        private float price;
        private int quantity;

        public InventoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public InventoryBuilder id(int id) {
            this.id = id;
            return this;
        }

        public InventoryBuilder price(float price) {
            this.price = price;
            return this;
        }

        public InventoryBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Inventory build() {
            return new Inventory(this);
        }
    }
    // getters and setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
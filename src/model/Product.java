package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class built as replica from provided class Part.java.
 * Naming conventions are identical from the provided
 * UML diagram
 */

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    public static ObservableList<Part> tieInAssociatedParts = FXCollections.observableArrayList();

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**Used InjelliJ's built in generate code for getters and setters
     * for entire constructor
     *
     *
     */

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public static ObservableList<Part> getAllAssociatedParts() {
        return tieInAssociatedParts;
    }

}
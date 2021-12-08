package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * This class is used to do the "heavy-lifting" of the data.
 * It is responsible for adding, editing, and deleting data.*/
public class Inventory {
    private static int trackPartID = 0;
    private static int trackProductID = 0;
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static ObservableList<Part> getAllParts() { return allParts; }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /** This method ensures a unique part ID.*/
    public static int getTrackPartID() {
        return ++trackPartID;
    }
    /**This method ensure a unique product ID.*/
    public static int getTrackProductID() {
        return ++trackProductID;
    }

    /**
     * Adds new part to inventory
     * @param newPart is the part that will be added
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds new product to inventory
     * @param newProduct is the product the will be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }


    /** lookupPart has two methods in the same class.
     * Overloaded according to the UML diagram provided.
     * This one goes through part ID numbers.
     */

    public static Part lookupPart(int searchedIDNumber) {

        for(int i = 0; i < allParts.size(); i++) {
            Part p = allParts.get(i);
            if(p.getId() == searchedIDNumber) {
                return p;
            }
        }
        return null;
    }

    /**This is the other lookupPart method. This one works by going
     * through part names. */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partLookup = FXCollections.observableArrayList();
        for(Part p : allParts){
            if(p.getName().toLowerCase().contains(partName.toLowerCase())) {
                partLookup.add(p);
            }
        }
        return partLookup;
    }


    /** lookupProduct has two methods in the same class.
     * Overloaded according to the UML diagram provided.
     * This one works by running through productID's.
     */
    public static Product lookupProduct(int productID) {
        for(int i = 0; i < allProducts.size(); i++) {
            Product p = allProducts.get(i);
            if(p.getId() == productID) {
                return p;
            }
        }
        return null;
    }

    /** lookupProduct has two methods in the same class.
     * Overloaded according to the UML diagram provided.
     * This one works by running through product names.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> returnedProducts = FXCollections.observableArrayList();
        for (Product p : allProducts) {
            if (p.getName().toLowerCase().contains(productName.toLowerCase())) {
                returnedProducts.add(p);
            }
        }
        return returnedProducts;
    }

    /**
     * Update part method matches corresponding unique ID
     * @param partToModify passes the through method
     * to use as the new/updated modified part
     */
    public static void updatePart(int index, Part partToModify) {
        allParts.set(index,partToModify);
    }

    /** Update product method updates changes to a product.*/
    public static void updateProduct(int index, Product ProductToModify) {
        allProducts.set(index, ProductToModify);
    }

    /** This method is used to delete a part.*/
    public static boolean deletePart(Part partToDelete) {
        allParts.remove(partToDelete);
        return true;
    }
    /** This method is used to delete a product.*/
    public static boolean deleteProduct(Product productSelected) {
        allProducts.remove(productSelected);
        return true;
    }

    /**This method is an input validation method.
     * This method ensures that user entries falls within
     * allowable value/parameters.
     * Follows logically row by row
     * for validation check
     */

    public static boolean validEntryCheck(String name, int inv, double price, int max, int min) throws NumberFormatException{
        boolean isValid = true;
        if (name.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("You must provide a valid name.");
            alert.show();
            isValid = false;
        }
        if (inv < min) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Inventory cannot be less than minimum requirement.");
            alert.show();
            isValid = false;
        }
        if (inv > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Inventory cannot be greater than maximum requirement");
            alert.show();
            isValid = false;
        }
        if (price < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("The price cannot be be less than 0.");
            alert.show();
            isValid = false;
        }
        if (min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Minimum cannot be greater than maximum.");
            alert.show();
            isValid = false;
        }
        return isValid;
    }
}

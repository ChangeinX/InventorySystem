package main;

import model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Objects;

/* This is the Main class that loads the application.
 */

public class Main extends Application {
     /** This is the start method. It launches the application. It also
      * passes in lambda function that calls exitApp method
      * event is consumed to prevent premature closure.
      */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/mainScreenController.fxml")));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            exitApp(primaryStage);
        });
    }

    /** Calls exitApp. Method is called when X in top corner
     * is pressed.
     */
    public void exitApp(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close Application");
        alert.setHeaderText("You're about to close the application!");
        alert.setContentText("Do you really wish to exit?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            primaryStage.close();
        }
    }

    /** Sample part and product data. These data are implemented in this method
     * as sample data to run the program.*/
    public static void main(String[] args) {
        Inventory.addPart(new InHouse(Inventory.getTrackPartID(), "Love", 0.00, 1, 1, 10, 42));
        Inventory.addPart(new InHouse(Inventory.getTrackPartID(), "Collapsed Nebula", 0.00, 1, 1, 10, 4242));
        Inventory.addPart(new OutSourced(Inventory.getTrackPartID(), "Forbidden Fruit: Apple", 9999.00, 1, 1, 10, "The Vatican"));
        Inventory.addPart(new OutSourced(Inventory.getTrackPartID(), "Hair Loss", 0.00, 10, 1, 10, "Age"));
        Inventory.addPart(new OutSourced(Inventory.getTrackPartID(), "Hair Growth", 9999.99, 9, 1, 10, "Lost Cause"));
        Inventory.addProduct(new Product(Inventory.getTrackProductID(), "Cosmic Joy", 9999.00, 1, 1, 2));
        Inventory.addProduct(new Product(Inventory.getTrackProductID(), "Sunshine State", 999.00, 1,1,2));
        Inventory.addProduct(new Product(Inventory.getTrackProductID(), "Nowhere Land", 0.00,99,1,100));

        launch(args);
    }
}

package controllers;

import model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**This is the mainScreen controller class.*/
/**RUNTIME ERROR: I accidentally imported awt functionality
 * instead of javafx when setting up buttons.*/
public class mainScreenController implements Initializable {
        /*Initially, I overburdened the project with
         * setting fx:id properties for buttons and
         * on action events. I wrote excess code and implemented
         * conditional statements inside button methods inside
         * onAction methods with an exhaustive stacktrace that
         * inserted excessive code nonsensically. Anecdotally,
         * through this tedium I found immeasurable benefits
         * of meta-programming. Alas, my skills are underdeveloped
         * to employ such a measure for this project.
         */
        @FXML
        private TextField searchByPart;

        @FXML
        private TableView<Part> partTableView;

        @FXML
        private TableColumn<Part, Integer> partIDTableColumn;

        @FXML
        private TableColumn<Part, String> partNameTableColumn;

        @FXML
        private TableColumn<Part, Integer> inventoryLevelTableColumn;

        @FXML
        private TableColumn<Part, Double> priceCostPerUnitTableColumn;

        @FXML
        private TextField searchbyProduct;

        @FXML
        private TableView<Product> productTableView;

        @FXML
        private TableColumn<Part, Integer> productIDTableColumn;

        @FXML
        private TableColumn<Part, String> productNameTableColumn;

        @FXML
        private TableColumn<Part, Integer> inventoryLevelTableColumnProduct;

        @FXML
        private TableColumn<Part, Double> priceCostPerUnitTableColumnProducts;

        @FXML
        private Pane scenePane;

        Stage primaryStage;

        /** This method launches the Add Product form window.*/
        @FXML
        void addProductForm(ActionEvent event) throws Exception {
                Parent root = FXMLLoader.load(getClass().getResource("../view/addProductForm.fxml"));
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setTitle("Add Product Form");
                primaryStage.setScene(scene);
                primaryStage.initModality(Modality.APPLICATION_MODAL);
                primaryStage.show();
        }

        @FXML
        /** This is the method that deletes a part. It works by identifying
         * the user selection, and follows with a confirmation prompt
         * to ensure the user desires to delete the part. Lastly,
         * if the user confirms to delete the part it calls the deletePart
         * method from the inventory class
         * @param deleteItem is the argument passed through the deletePart
         * method from the inventory class based on the user selection.*/
        void deletePart(ActionEvent event) {
                Part deleteItem = partTableView.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove Part Selected");
                alert.setContentText("Do you really wish to delete this part?");
                alert.showAndWait().ifPresent(proceed -> {
                        if (proceed == ButtonType.OK) {
                                Inventory.deletePart(deleteItem);
                        }
                });

        }

        @FXML
        /** This is the method that deletes a product. It works by identifying
         * the user selection, and follows with a confirmation prompt
         * to ensure the user desires to delete the product. Lastly,
         * if the user confirms to delete the product it calls the deleteProduct
         * method from the inventory class
         * @param itemSelected is the argument passed through the deleteProduct
         * method from the inventory class based on the user selection.*/
        public void deleteProduct(ActionEvent actionEvent) {
                Product itemSelected = productTableView.getSelectionModel().getSelectedItem();

                if (itemSelected == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("No Product Selected");
                        alert.setContentText("A product was not selected to be deleted.");
                } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setContentText("Confirm you would like to delete the selected product.");
                        Optional<ButtonType> response = alert.showAndWait();

                        if (response.isPresent() && response.get() == ButtonType.OK) {
                                ObservableList<Part> associated = itemSelected.getAllAssociatedParts();

                                if (associated.size() >=1) {
                                       Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                       alert2.setTitle("Parts In Inventory Remain");
                                       alert2.setContentText("You must delete associated parts with this product before product deletion.");
                                } else {
                                        Inventory.deleteProduct(itemSelected);
                                }
                        }
                }
        }

        @FXML
        /** This is the modifyPartForm method. If the user selects an item
         * it launches the modifyPartForm screen and populates the fields
         * with data to be modified.*/
        void modifyPartForm(ActionEvent event) throws Exception{
                // All classes and methods starts with capital letters

                if (partTableView.getSelectionModel().getSelectedItem() != null) {
                        FXMLLoader generic = new FXMLLoader(getClass().getResource("../view/modifyPartForm.fxml"));
                        Parent mainScreenController = generic.load();
                        Scene scene = new Scene(mainScreenController, 600, 400);
                        Stage primaryStage = new Stage();
                        primaryStage.setTitle("Modify Part Form");
                        primaryStage.setScene(scene);
                        primaryStage.initModality(Modality.APPLICATION_MODAL);
                        primaryStage.show();
                        modifyPartForm controller = generic.getController();
                        selectedPart = partTableView.getSelectionModel().getSelectedItem();

                        controller.selectedPart(selectedPart);

                } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("No selection made.");
                        alert.setContentText("Please select a part");
                        alert.showAndWait();
                }
        }

        @FXML
        /** This is the modifyProductForm method. If the user selects an item
         * it launches the modifyProductForm screen and populates the fields
         * with data to be modified.*/
        void modifyProductForm(ActionEvent event) throws Exception{
                if (productTableView.getSelectionModel().getSelectedItem() == null) {
                        Alert alert = new Alert (Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Selected a Product to Modify.");
                        alert.showAndWait();

                } else {

                        FXMLLoader generic = new FXMLLoader(getClass().getResource("../view/modifyProductForm.fxml"));
                        Parent mainScreenController = generic.load();
                        Scene scene = new Scene(mainScreenController, 1080, 600);
                        Stage primaryStage = new Stage();
                        primaryStage.setTitle("Modify Product Form");
                        primaryStage.setScene(scene);
                        primaryStage.initModality(Modality.APPLICATION_MODAL);
                        primaryStage.show();
                        modifyProductForm controller = generic.getController();
                        Product selectItem = productTableView.getSelectionModel().getSelectedItem();
                        controller.selectedItem(selectItem);
                }
        }
        /** This method closes the application.*/
        public void exitApp(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Close Application");
                alert.setHeaderText("You're about to close the application!");
                alert.setContentText("Do you really wish to exit?");

                if(alert.showAndWait().get() == ButtonType.OK) {
                        primaryStage = (Stage) scenePane.getScene().getWindow();
                        primaryStage.close();
                }
        }

        /** This method loads the add part form screen. This is where a user can
         * add parts to the inventory.*/
        public void addPartForm(ActionEvent actionEvent) throws Exception {
                Parent root = FXMLLoader.load(getClass().getResource("../view/addPartForm.fxml"));

                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setTitle("Add Part Form");
                /** Error Note: First attempt had an error. Instead of
                 *  passing scene as defined argument, I had placed
                 * primaryStage.setScene(new Scene(root, 1080, 600)); from Main.java
                 */
                primaryStage.setScene(scene);
                primaryStage.initModality(Modality.APPLICATION_MODAL);
                primaryStage.show();
        }

        /** This method searches for parts.
         *The rest of the project seemed vanilla compared
         * to creating a search method, but once one technique
         * was established, I was able to reuse the code in other
         * search fields. It is worth pointing out that the provided
         * documentation for the dynamic search method provided
         * in the course was inadequate. I had multiple appointments
         * with several course instructors that were more than helpful,
         * but all had one conclusion: The dynamic search method fails
         * 10 times out of 10 in this application.
         */
        @FXML
        void searchPart(ActionEvent event) {
                if(!searchByPart.getText().isBlank()) {
                        try {
                                int partSearchID = Integer.parseInt(searchByPart.getText());
                                Part a = Inventory.lookupPart(partSearchID);
                                if(a == null) {
                                        throw new NumberFormatException();
                                }
                                partTableView.getSelectionModel().select(a);
                        } catch (NumberFormatException e) {
                                String partSearchName = searchByPart.getText();
                                ObservableList<Part> p = Inventory.lookupPart(partSearchName);
                                if(p.size() == 0) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Information");
                                        alert.setContentText("No matching parts found.");
                                        alert.showAndWait();
                                        return;
                                }
                                partTableView.setItems(p);
                        }
                }
                else {
                        partTableView.setItems(Inventory.getAllParts());
                }
        }

        /** This method searches products.*/
        public void productSearch(ActionEvent event) {
                if(!searchbyProduct.getText().isBlank()) {
                        try {
                                int productSearchID = Integer.parseInt(searchbyProduct.getText());
                                Product a = Inventory.lookupProduct(productSearchID);
                                if(a == null) {
                                        throw new NumberFormatException();
                                }
                                productTableView.getSelectionModel().select(a);
                        } catch (NumberFormatException e) {
                                String productSearchName = searchbyProduct.getText();
                                ObservableList<Product> p = Inventory.lookupProduct(productSearchName);
                                if(p.size() == 0) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Information");
                                        alert.setContentText("No matching parts found.");
                                        alert.showAndWait();
                                        return;
                                }
                                productTableView.setItems(p);
                        }
                }
                else {
                        productTableView.setItems(Inventory.getAllProducts());
                }
        }

        private static Part selectedPart;

        @Override
        /**This method binds product and part tables. This allows the tables
         * to be populated with data.*/
        public void initialize(URL url, ResourceBundle resourceBundle) {
                /* Binding part table columns*/
                partIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                partNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                inventoryLevelTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                priceCostPerUnitTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                partTableView.setItems(Inventory.getAllParts());

                /*Binding product table columns */
                productIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                inventoryLevelTableColumnProduct.setCellValueFactory(new PropertyValueFactory<>("stock"));
                priceCostPerUnitTableColumnProducts.setCellValueFactory(new PropertyValueFactory<>("price"));
                productTableView.setItems(Inventory.getAllProducts());
        }
}

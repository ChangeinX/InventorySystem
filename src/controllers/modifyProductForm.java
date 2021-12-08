package controllers;

import model.Inventory;
import model.Part;
import model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the modifyProductForm class. It handles modifying products, associated part data,
 * and user input validation.*/
public class modifyProductForm implements Initializable {

    private ObservableList<Part> partsAssociated = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> tableAllPartOptions;

    @FXML
    private TableColumn<Part, Integer> columnPartID;

    @FXML
    private TableColumn<Part, String> columnPartName;

    @FXML
    private TableColumn<Part, Integer> columnInvLvl;

    @FXML
    private TableColumn<Part, Double> columnPrice;

    @FXML
    private TableView<Part> tableAssociatedPart;

    @FXML
    private TableColumn<Part, Integer> columnAssociatedPartID;

    @FXML
    private TableColumn<Part, String> columnAssociatedPartName;

    @FXML
    private TableColumn<Part, Integer> columnAssociatedInvLvl;

    @FXML
    private TableColumn<Part, Double> columnAssociatedPrice;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField productName;

    @FXML
    private TextField productInv;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productMax;

    @FXML
    private TextField productMin;

    @FXML
    private TextField productID;

    @FXML
    private TextField tfSearchPart;
    private Product product;

    @FXML
    void searchAllParts(ActionEvent event) {
        if(!tfSearchPart.getText().isBlank()) {
            try {
                int partSearchID = Integer.parseInt(tfSearchPart.getText());
                Part a = Inventory.lookupPart(partSearchID);
                if(a == null) {
                    throw new NumberFormatException();
                }
                tableAllPartOptions.getSelectionModel().select(a);
            } catch (NumberFormatException e) {
                String partSearchName = tfSearchPart.getText();
                ObservableList<Part> p = Inventory.lookupPart(partSearchName);
                if(p.size() == 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setContentText("No matching parts found.");
                    alert.showAndWait();
                    return;
                }
                tableAllPartOptions.setItems(p);
            }
        }
        else {
            tableAllPartOptions.setItems(Inventory.getAllParts());
        }
    }
    @FXML
    /** This method saves modified product data. If user entries
     * in text fields pass input validation requirements, it is
     * added.*/
    void saveProductData(ActionEvent event) {
        String mProductName = productName.getText();
        Double mProductPrice;
        int mProductInv;
        int mMax;
        int mMin;

        /* Error catching for user input. */
        try {
            mProductInv = Integer.parseInt(productInv.getText());
        } catch (NumberFormatException exception) {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Must be a number more than min and less than max.");
            alert.showAndWait();
            return;
        }
        try {
            mProductPrice = Double.parseDouble(productPrice.getText());
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Must be a number in X.XX format.");
            alert.showAndWait();
            return;
        }
        try {
            mMax = Integer.parseInt(productMax.getText());
        } catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Must be a number and more than min.");
            alert.showAndWait();
            return;
        }
        try {
            mMin = Integer.parseInt(productMin.getText());
        } catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Must be a number and less than max.");
            alert.showAndWait();
            return;
        }
        if(validEntryCheck(mProductName, mProductInv, mProductPrice, mMin, mMax)) {
            Product mProduct = new Product(Inventory.getTrackProductID(), mProductName, mProductPrice, mProductInv, mMin, mMax);
            int productIndex = Inventory.getAllProducts().indexOf(this.product);
            Inventory.updateProduct(productIndex, mProduct);
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    /** This method cancels adding a product. If called upon,
     * it also close the window as per task requirement.*/
    void cancelAddProduct(ActionEvent event) {
        /** get a handle to stage
         */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Product Modification");
        alert.setContentText("Do you wish to proceed to cancel modifying the project?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage stage = (Stage) btnCancel.getScene().getWindow();
                stage.close();
            }
        });
    }

    /** This method removes an associated part. If no selection
     * was made, an alert is generated to the user displaying
     * that a valid selection wasn't made. If a valid entry is
     * made the user is prompt with a confirmation box. If the
     * user selects OK, the system registers this response
     * and removes the associated part.
     * */
    @FXML
    void removeAssociatedPart(ActionEvent event) {
        Part p = tableAssociatedPart.getSelectionModel().getSelectedItem();
        if(p == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No valid selection made.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Remove associated part?");
            Optional<ButtonType> answer = alert.showAndWait();

            if(answer.isPresent() && answer.get() == ButtonType.OK) {
                partsAssociated.remove(p);
                tableAssociatedPart.setItems(partsAssociated);
            }
        }
    }

    /** This method adds a part from the top table all parts view
     * to the lower table as an associated part for a product.*/
    @FXML
    void btnAddProduct(ActionEvent event) {
        Part itemSelected = tableAllPartOptions.getSelectionModel().getSelectedItem();
        if(itemSelected != null) {
            partsAssociated.add(itemSelected);
            tableAssociatedPart.setItems(partsAssociated);
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText("Select a part");
            a.showAndWait();
        }
    }

    /** This is used for input validation.
     * This is reused code from addPartForm.java
     * used for input handling.
     */
    public boolean validEntryCheck(String name, int inv, double price, int max, int min) throws NumberFormatException{
        if (name.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("You must provide a valid name.");
        }
        if (inv < min) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Inventory cannot be less than minimum requirement.");
        }
        if (inv > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Inventory cannot be greater than maximum requirement");
        }
        if (price < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("The price cannot be be less than 0.");
        }
        if (min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Minimum cannot be greater than maximum.");
        }
        return true;
    }

    /** This method takes data from the mainScreenController.
     * It uses this data to populate the TextFields in this form.*/
    public void selectedItem(Product ProductToModify) {
        this.product = ProductToModify;
        productID.setText(Integer.toString(product.getId()));
        productName.setText(product.getName());
        productInv.setText(Integer.toString(product.getStock()));
        productPrice.setText(Double.toString(product.getPrice()));
        productMax.setText(Integer.toString(product.getMax()));
        productMin.setText(Integer.toString(product.getMin()));
        partsAssociated = product.getAllAssociatedParts();
        tableAssociatedPart.setItems(partsAssociated);
    }

    @Override
    /** This method initializes the tables with associated part/product data.*/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableAllPartOptions.setItems(Inventory.getAllParts());

        tableAssociatedPart.setItems(partsAssociated);

        columnAssociatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnAssociatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAssociatedInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnAssociatedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}

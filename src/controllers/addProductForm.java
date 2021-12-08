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

/** This is the addProductForm class. This class handles adding
 * a new product, as well as on screen functionality as defined per
 * task requirements. RUNTIME ERROR: When setting up the unique
 * part/product ID I created a the TextField as an int according to
 * it's fx:id. I resolved by removing obsolete code.*/
public class addProductForm implements Initializable {
    @FXML
    private TableView<Part> tableAllPartOptions;

    @FXML
    private TableColumn<Part, Integer> allPartIDColumn;

    @FXML
    private TableColumn<Part, String> allPartNamecolumn;

    @FXML
    private TableColumn<Part, Integer> allInvLvlColumn;

    @FXML
    private TableColumn<Part, Double> allPriceColumn;

    @FXML
    private TableView<Part> tableAssociatedPart;

    @FXML
    private TableColumn<Part, Integer> associatedPartIDColumn;

    @FXML
    private TableColumn<Part, String> associatedPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> associatedInvLvlColumn;

    @FXML
    private TableColumn<Part, Double> associatedPriceColumn;

    @FXML
    private TextField tfProductName;

    @FXML
    private TextField tfProductIInv;

    @FXML
    private TextField tfProductPriice;

    @FXML
    private TextField tfProductMax;

    @FXML
    private TextField tfProductMin;

    @FXML
    private TextField tfPartSearch;

    @FXML
    private Button btnCancel;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**This is the search part method.
     * Recycled code from mainScreenController.java class.
     */

    public void partSearch(ActionEvent event) {
        if(!tfPartSearch.getText().isBlank()) {
            try {
                int partSearchID = Integer.parseInt(tfPartSearch.getText());
                Part a = Inventory.lookupPart(partSearchID);
                if(a == null) {
                    throw new NumberFormatException();
                }
                tableAllPartOptions.getSelectionModel().select(a);
            } catch (NumberFormatException e) {
                String partSearchName = tfPartSearch.getText();
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
    /** This method saves user input. It verifies the entry is
     * valid then adds the product. */
    void saveProductData(ActionEvent event) throws NumberFormatException {
        String mProductName = tfProductName.getText();
        Double mProductPrice;
        int mProductInv;
        int mMax;
        int mMin;

        /* Error catching for user input.
         */
        try {
            mProductInv = Integer.parseInt(tfProductIInv.getText());
        } catch (NumberFormatException exception) {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Must be a number more than min and less than max.");
            alert.showAndWait();
            return;
        }
        try {
            mProductPrice = Double.parseDouble(tfProductPriice.getText());
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Must be a number in X.XX format.");
            alert.showAndWait();
            return;
        }
        try {
            mMax = Integer.parseInt(tfProductMax.getText());
        } catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Must be a number and more than min.");
            alert.showAndWait();
            return;
        }
        try {
            mMin = Integer.parseInt(tfProductMin.getText());
        } catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Must be a number and less than max.");
            alert.showAndWait();
            return;
        }
        if(validEntryCheck(mProductName, mProductInv, mProductPrice, mMin, mMax)) {
            Product mProduct = new Product(Inventory.getTrackProductID(), mProductName, mProductPrice, mProductInv, mMin, mMax);
            Inventory.addProduct(mProduct);
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    /** This method cancel adding a product. As per task requirements,
     * it closes when this method is called via user selection (button pressed).
     * */
    void cancelAddProduct(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    /** This method removes an associated part. If no selection
     * was made, an alert is generated to the user displaying
     * that a valid selection wasn't made. If a valid entry is
     * made the user is prompt with a confirmation box. If the
     * user selects OK, the system registers this response
     * and removes the associated part.*/
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
                associatedParts.remove(p);
                tableAssociatedPart.setItems(associatedParts);
            }

        }
    }

    @FXML
    /** This method adds an associated part. A part is added
     * when a user selects a part to be added and presses the
     * add button.*/
    void btnAddProduct(ActionEvent event) {
        Part itemSelected = tableAllPartOptions.getSelectionModel().getSelectedItem();
        if(itemSelected != null) {
            associatedParts.add(itemSelected);
            tableAssociatedPart.setItems(associatedParts);
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText("Select a part");
            a.showAndWait();
        }
    }
    /** This method verifies valid entries were made. */
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

    @Override
    /** This method populates the tables on the screen. */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartNamecolumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allInvLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableAllPartOptions.setItems(Inventory.getAllParts());

        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedInvLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableAssociatedPart.setItems(associatedParts);
    }
}

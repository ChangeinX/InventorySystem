package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.Inventory;
import model.Part;
import model.InHouse;
import model.OutSourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** This is the addPartForm class. It handles adding parts to inventory
 * as well as input validation checks. */
public class addPartForm implements Initializable {
    @FXML
    private TextField tfPartPrice;

    @FXML
    private TextField tfPartMaxCost;

    @FXML
    private TextField tfPartMachineID;

    @FXML
    private TextField tfPartName;

    @FXML
    private TextField tfPartInventory;

    @FXML
    private TextField tfPartMinCost;

    @FXML
    private RadioButton rdBtnInHouse;

    @FXML
    private RadioButton rdBtnOutSourced;

    @FXML
    private Label lblRadioChance;

    @FXML
    private Button btnCancelAddPart;

    @FXML
    private ToggleGroup rdBtnChange;



    @FXML
    /** This method cancels adding a part. This method also closes
     * the window when called (button pressed).
     */
    void cancelAddPart(ActionEvent event) {
        Stage stage = (Stage) btnCancelAddPart.getScene().getWindow();
        stage.close();
    }

    @FXML
    /**This method saves a part. It is called when the
     * save button is pressed. */
    void saveAddPart(ActionEvent event) throws NumberFormatException {
        /*Set variables to hold in memory.*/

        String entryName = tfPartName.getText();
        int entryInventory;
        double entryPrice;
        int entryMin;
        int entryMax;
        int machineID;
        String entryCompanyName;

        /*Create conditional fields for entry and validation check.
         * Access toggle group
         */
        /*Initially, when running the application and adding/modifying parts
         * I was getting duplicate records.
         * The error persisted in the modify part form, because I re-used
         * the same code there.
         *
         * After modifying some code, I got this duplication error again.
         * I placed the the outSourced conditional statement to add part
         * to inventory outside of the original if statement.
         *
         */

        try {
            entryInventory = Integer.parseInt(tfPartInventory.getText());
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Inventory must be a valid number between min and max.");
            alert.showAndWait();
            return;
        } try {
            entryPrice = Double.parseDouble(tfPartPrice.getText());
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Enter price in appropriate currency format to the hundredths place. EX: $0.00");
            alert.showAndWait();
            return;
        } try {
            entryMin = Integer.parseInt(tfPartMinCost.getText());
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Min must be a number and less than max.");
            alert.showAndWait();
            return;
        } try {
            entryMax = Integer.parseInt(tfPartMaxCost.getText());
        } catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Max must be a number and greater than min.");
            alert.showAndWait();
            return;
        }
        if (Inventory.validEntryCheck(entryName, entryInventory, entryPrice, entryMax, entryMin)) {
            if (rdBtnInHouse.isSelected()) {
                try {
                    machineID = Integer.parseInt(tfPartMachineID.getText());
                } catch (NumberFormatException exception) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setContentText("Machine ID must be a number.");
                    alert.showAndWait();
                    return;
                }
                Part EntryInHouse = new InHouse(Inventory.getTrackPartID(), entryName, entryPrice, entryInventory,
                        entryMin, entryMax, machineID);
                Inventory.addPart(EntryInHouse);
            } else {
                entryCompanyName = tfPartMachineID.getText();
                if(entryCompanyName.isBlank()) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setContentText("Company Name cannot be blank");
                    a.showAndWait();
                    return;
                }
                Part outSourcedPart = new OutSourced(Inventory.getTrackPartID(), entryName, entryPrice, entryInventory,
                        entryMin, entryMax, entryCompanyName);
                Inventory.addPart(outSourcedPart);
            }
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("../view/mainScreenController.fxml"));
                Stage stage = (Stage) tfPartName.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
        }
    }

    /** Method used to change radio button selection
     *
     */
    public void rdBtnLabelChange() {
        if(rdBtnChange.getSelectedToggle().equals(rdBtnInHouse)) {
            lblRadioChance.setText("Machine ID");
        } else if (rdBtnChange.getSelectedToggle().equals(rdBtnOutSourced)) {
                lblRadioChance.setText("Company Name");
            }
        }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}

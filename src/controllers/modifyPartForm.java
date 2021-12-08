package controllers;

import model.Inventory;
import model.Part;
import model.InHouse;
import model.OutSourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/** This is the modify part form class. It handles modifying parts,
 * as well as user input validation.*/
public class modifyPartForm implements Initializable {
    Part part;

    @FXML
    private RadioButton rdBtnInHouse;

    @FXML
    private ToggleGroup rdBtnChange;

    @FXML
    private RadioButton rdBtnOutSourced;

    @FXML
    private Label lblRadioChance;

    @FXML
    private TextField tfPartID;

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
    private Button btnCancelAddPart;

    /**This method cancels adding a part. When called upon,
     * it also close the window as per task requirement.*/
    @FXML
    void cancelAddPart(ActionEvent event) {
        Stage stage = (Stage) btnCancelAddPart.getScene().getWindow();
        stage.close();
    }

    /** Method used to change radio button selection.
     */
    @FXML
    void rdBtnChangeLabel(ActionEvent event) {
        if(rdBtnChange.getSelectedToggle().equals(rdBtnInHouse)) {
            lblRadioChance.setText("Machine ID");
        } else if (rdBtnChange.getSelectedToggle().equals(rdBtnOutSourced)) {
            lblRadioChance.setText("Company Name");
        }
    }

    /**Mostly recycled code from addPartForm.java
     * with a few modifications for this class
     * (modifyPartForm.java)
     */
    @FXML
    void saveAddPart(ActionEvent event) throws NumberFormatException {
        String entryName = tfPartName.getText();
        int entryInventory;
        double entryPrice;
        int entryMin;
        int entryMax;
        int machineID;
        String entryCompanyName;

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
                int partIndex = Inventory.getAllParts().indexOf(this.part);

                Inventory.updatePart(partIndex, EntryInHouse);
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
                int partIndex = Inventory.getAllParts().indexOf(this.part);
                Inventory.updatePart(partIndex, outSourcedPart);
            }
            Stage stage = (Stage) btnCancelAddPart.getScene().getWindow();
            stage.close();
        }
    }

    /**This method is used to acquire part information.
     * @param partToModify from the mainScreenController.java
     */

    public void selectedPart (Part partToModify) {
        this.part = partToModify;

        if(partToModify instanceof InHouse) {
            rdBtnInHouse.setSelected(true);
            lblRadioChance.setText("Machine ID");
            InHouse in_house = (InHouse)partToModify;
            tfPartID.setText(Integer.toString(in_house.getId()));
            tfPartName.setText(in_house.getName());
            tfPartPrice.setText(Double.toString(in_house.getPrice()));
            tfPartInventory.setText(Integer.toString(in_house.getStock()));
            tfPartMaxCost.setText(Integer.toString(in_house.getMax()));
            tfPartMinCost.setText(Integer.toString(in_house.getMin()));
            tfPartMachineID.setText(Integer.toString(in_house.getMachineID()));
        } else if (partToModify instanceof OutSourced) {
            rdBtnInHouse.setSelected(false);
            rdBtnOutSourced.setSelected(true);
            lblRadioChance.setText("Company Name");
            OutSourced out_sourced = (OutSourced)partToModify;
            tfPartID.setText(Integer.toString(out_sourced.getId()));
            tfPartName.setText(out_sourced.getName());
            tfPartPrice.setText(Double.toString(out_sourced.getPrice()));
            tfPartInventory.setText(Integer.toString(out_sourced.getStock()));
            tfPartMaxCost.setText(Integer.toString(out_sourced.getMax()));
            tfPartMinCost.setText(Integer.toString(out_sourced.getMin()));
            tfPartMachineID.setText((out_sourced.getCompanyName()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.controller;

import at.htlstp.syp.mmtasking.db.MMTDAO;
import at.htlstp.syp.mmtasking.db.MMTDBException;
import at.htlstp.syp.mmtasking.model.Appointment;
import at.htlstp.syp.mmtasking.model.Location;
import at.htlstp.syp.mmtasking.model.TaskPriority;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

/**
 * FXML Controller class
 *
 * @author josi
 */
public class AddAppController implements Initializable {

    @FXML
    private JFXTextField tf_termin;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXTimePicker time;
    @FXML
    private JFXButton addApp;

    MMTDAO dao = new MMTDAO();
    @FXML
    private JFXTextArea note;
    @FXML
    private ChoiceBox<Location> cb_Loc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillLocs();
    }

    @FXML
    private void doAddApp(ActionEvent event) {
        
        Appointment app = null;
        try {
            app = new Appointment(tf_termin.getText(), new Location("not implemented"), LocalDateTime.of(date.getValue(), time.getValue()), note.getText());
        } catch (Exception e) {
               Alert a = new Alert(Alert.AlertType.ERROR);
               a.setTitle("Error");
               a.setContentText("Überprüfen Sie bitte Ihre Eingaben");
               a.show();
        }

        try {
            dao.insertAppointment(app);
        } catch (MMTDBException ex) {
            Logger.getLogger(AddAppController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillLocs() {

        ObservableList<Location> locs = FXCollections.observableArrayList();
        //locs.addAll(dao.getAllLocation());
        locs.add(new Location("Irnfritz"));
        cb_Loc.setItems(locs);
    }
}

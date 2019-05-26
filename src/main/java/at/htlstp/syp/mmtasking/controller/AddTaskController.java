package at.htlstp.syp.mmtasking.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import at.htlstp.syp.mmtasking.db.MMTDAO;
import at.htlstp.syp.mmtasking.db.MMTDBException;
import at.htlstp.syp.mmtasking.model.Appointment;
import at.htlstp.syp.mmtasking.model.TaskPriority;
import static at.htlstp.syp.mmtasking.model.Appointment_.location;
import at.htlstp.syp.mmtasking.model.Category;
import at.htlstp.syp.mmtasking.model.Location;
import at.htlstp.syp.mmtasking.model.Task;
import at.htlstp.syp.mmtasking.model.TaskPriority;
import static at.htlstp.syp.mmtasking.model.Task_.category;
import static at.htlstp.syp.mmtasking.model.Task_.note;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import eu.hansolo.enzo.notification.Notification;
import java.net.URL;
import java.time.LocalDateTime;
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
import javafx.scene.layout.Priority;

/**
 * FXML Controller class
 *
 * @author josi
 */
public class AddTaskController implements Initializable {

    @FXML
    private JFXDatePicker dateBegin;
    @FXML
    private JFXTimePicker timeBegin;
    @FXML
    private JFXTextField taskName;
    @FXML
    private JFXDatePicker dateEnd;
    @FXML
    private JFXTimePicker timeEnd;
    @FXML
    private JFXCheckBox cbDeletable;
    @FXML
    private JFXCheckBox cbHoch;
    @FXML
    private JFXCheckBox cbNiedrig;
    @FXML
    private JFXCheckBox cbMittel;
    @FXML
    private JFXTextArea taNote;
    @FXML
    private JFXButton addTask;
    @FXML
    private ChoiceBox<Location> cbLocs;
    @FXML
    private ChoiceBox<Category> cbCategory;

    private MMTDAO dao = MMTDAO.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpEnv();
        taskName.requestFocus();
    }

    @FXML
    private void doAddTask(ActionEvent event) {

        Task t = null;

        try {
            LocalDateTime von = LocalDateTime.of(dateBegin.getValue(), timeBegin.getValue());
            LocalDateTime bis = LocalDateTime.of(dateEnd.getValue(), timeEnd.getValue());
            t = new Task(taskName.getText(), von, bis, cbCategory.getSelectionModel().getSelectedItem().toString(),
                    TaskPriority.HIGH, taNote.getText(), true, true);

        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText("Überprüfen Sie bitte Ihre Eingaben");
            a.show();
        }

        try {
            int lastID = dao.getAllTasks()
                    .stream()
                    .mapToInt(task -> task.getId())
                    .max()
                    .getAsInt();
            t.setId(lastID + 1);
            dao.insertTask(t);
        } catch (MMTDBException ex) {
            Logger.getLogger(AddAppController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setUpEnv() {

        cbLocs.getItems().addAll(dao.getAllLocations());

        cbCategory.getItems().addAll(dao.getAllCategories());

        cbHoch.setOnMouseClicked((event) -> {
            handlePrio(TaskPriority.HIGH);
        });
        cbMittel.setOnMouseClicked((event) -> {
            handlePrio(TaskPriority.MEDIUM);
        });
        cbNiedrig.setOnMouseClicked((event) -> {
            handlePrio(TaskPriority.LOW);
        });

    }

    private void handlePrio(TaskPriority priority) {
        cbHoch.setSelected(false);
        cbMittel.setSelected(false);
        cbNiedrig.setSelected(false);

        switch (priority) {
            case HIGH:
                cbHoch.setSelected(true);
                break;
            case MEDIUM:
                cbMittel.setSelected(true);
                break;
            case LOW:
                cbNiedrig.setSelected(true);
                break;
        }

    }

}

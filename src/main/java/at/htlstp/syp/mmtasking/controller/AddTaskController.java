package at.htlstp.syp.mmtasking.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import at.htlstp.syp.mmtasking.db.MMTDAO;
import at.htlstp.syp.mmtasking.db.MMTDBException;
import at.htlstp.syp.mmtasking.model.Category;
import at.htlstp.syp.mmtasking.model.Fahrt;
import at.htlstp.syp.mmtasking.model.Location;
import at.htlstp.syp.mmtasking.model.Task;
import at.htlstp.syp.mmtasking.model.TaskPriority;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import eu.hansolo.enzo.notification.Notification.Notifier;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    }

    @FXML
    private void doAddTask(ActionEvent event) {

        Task t = null;

        try {
            LocalDateTime von = LocalDateTime.of(dateBegin.getValue(), timeBegin.getValue());
            LocalDateTime bis = LocalDateTime.of(dateEnd.getValue(), timeEnd.getValue());
            Location location = cbLocs.getSelectionModel().getSelectedItem();
            Fahrt fahrt = dao.getFahrtNach(location);
            t = new Task(taskName.getText(), von, bis, fahrt, cbCategory.getSelectionModel().getSelectedItem().toString(),
                    (cbHoch.isSelected() ? TaskPriority.HIGH : cbMittel.isSelected() ? TaskPriority.MEDIUM : cbNiedrig.isSelected() ? TaskPriority.LOW : TaskPriority.LOW), taNote.getText(), cbDeletable.isSelected(), false);

        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }

        try {
            int lastID = dao.getAllTasks()
                    .stream()
                    .mapToInt(task -> task.getId())
                    .max()
                    .getAsInt();
            t.setId(lastID + 1);
            dao.insertTask(t);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Erfolgreich");
            a.setContentText("Der neue Task wurde erfolgreich angelegt!");
            a.showAndWait();
            Notifier.INSTANCE.notifyInfo("Task hinzugefügt", "Der neue Task wurde erfolreich angelegt!");
        } catch (MMTDBException ex) {
            Logger.getLogger(AddAppController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setUpEnv() {

        dateBegin.setValue(LocalDate.now());
        timeBegin.setValue(LocalTime.now());

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
        taskName.requestFocus();
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.controller;

import at.htlstp.syp.mmtasking.model.Appointment;
import at.htlstp.syp.mmtasking.model.Category;
import at.htlstp.syp.mmtasking.model.Task;
import at.htlstp.syp.mmtasking.model.TaskPriority;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import eu.hansolo.enzo.notification.Notification.Notifier;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author josi
 */
public class MainAppController implements Initializable {

    @FXML
    private Label lblAutoLogout;
    @FXML
    private Label lblCurrentUser;
    @FXML
    private Label lblDate;
    @FXML
    private ListView<Task> lvTaskM;
    @FXML
    private Label lblTime;
    @FXML
    private ListView<Appointment> lvTerminM;
    @FXML
    private Label lblFahrzeit;
    @FXML
    private ChoiceBox<Category> cbKategorie;
    @FXML
    private ChoiceBox<?> cbOrt;
    @FXML
    private Button btnAuswerten;
    @FXML
    private ChoiceBox<String> cbPriority;
    @FXML
    private JFXTextField tfTaskD;
    @FXML
    private JFXDatePicker dateVon;
    @FXML
    private JFXTimePicker timeVon;
    @FXML
    private JFXDatePicker dateBis;
    @FXML
    private JFXTimePicker timeBis;
    @FXML
    private JFXTextField tfOrt;
    @FXML
    private JFXTextArea taComment;
    @FXML
    private JFXCheckBox cbHoch;
    @FXML
    private JFXCheckBox cbMittel;
    @FXML
    private JFXCheckBox cbNiedrig;
    @FXML
    private JFXTextField tfKategorieD;
    @FXML
    private JFXCheckBox cbDeleteable;
    @FXML
    private JFXListView<Task> lvAusstehendeTasks;
    @FXML
    private JFXDatePicker dateVonAnalyse;
    @FXML
    private JFXDatePicker dateBisAnalyse;

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("kk:mm:ss");
    private static final DateTimeFormatter dateFormatterLong = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy");
    private Timeline clock;
    private Timeline date;
    private Timeline autologout;
    private Instant logoutTime;
    @FXML
    private TabPane tabPane;
    
    private List<JFXCheckBox> checkboxes = Arrays.asList(cbHoch, cbMittel, cbNiedrig);
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnFinalize;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDateTime current;
        
        initTimeTimeline();
        initDateTimeline();
        
        initFinalizing();
        
        btnFinalize.setOnAction((ActionEvent e) -> {
            Task t = lvAusstehendeTasks.getSelectionModel().getSelectedItem();
            t.finalizeTask();
            lvAusstehendeTasks.getItems().remove(t);
        });
        
        btnEdit.setOnAction(e -> {
            Task task = lvAusstehendeTasks.getSelectionModel().getSelectedItem();
            tfTaskD.setText(task.getTitle());
            tfKategorieD.setText(task.getCategory());
            dateVon.setValue(task.getBeginning().toLocalDate());
            dateBis.setValue(task.getEnd().toLocalDate());
            timeVon.setValue(task.getBeginning().toLocalTime());
            timeBis.setValue(task.getEnd().toLocalTime());
            changePriority(task.getPriority());
            cbDeleteable.setSelected(task.isDeletable());
            taComment.setText(task.getNote());
            
            tabPane.getSelectionModel().select(1);
        });
        
        
        
//        LocalDateTime future = LocalDateTime.now().plusMinutes(15);
//        logoutTime = Instant.now();
//        autologout = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
//            logoutTime.minusSeconds(1);
//            lblAutoLogout.setText(timeFormatter.format(future.minus(LocalTime.now().toSecondOfDay())));
//        }));
//        autologout.setCycleCount(15 * 60);
//        autologout.play();

        // Autologut
//        Timer timer = new Timer();
//        timer.sch
//        
//        
//        logoutTime = Instant.now().plus(java.time.Duration.ofMinutes(15));
//        java.time.Duration.
//        autologout = new Timeline(new KeyFrame(Duration.ZERO, e -> {
//            System.out.println(logoutTime);
//            lblAutoLogout.setText(timeFormatter.format(logoutTime));
//            logoutTime = logoutTime.minusSeconds(1);
//        }), new KeyFrame(Duration.seconds(1)));
//        autologout.setCycleCount(15 * 60);
//        autologout.play();
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        setUpTaskM();

        lvTerminM.setItems(appointments);
    }

    private void initFinalizing() {
        List<Task> taskliste = new ArrayList<>();
        taskliste.add(new Task("Task 1", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, "Fixen", TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 2", LocalDateTime.now(), LocalDateTime.now().plusDays(4), null, "Priorisieren", TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, "Aufsetzen", TaskPriority.HIGH, "Commentar 3", true, false));
        taskliste.add(new Task("Task 4", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, "Fixen", TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 5", LocalDateTime.now(), LocalDateTime.now().plusDays(4), null, "Priorisieren", TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 6", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, "Aufsetzen", TaskPriority.HIGH, "Commentar 3", true, false));
        taskliste.add(new Task("Task 7", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, "Fixen", TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 8", LocalDateTime.now(), LocalDateTime.now().plusDays(4), null, "Priorisieren", TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 9", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, "Aufsetzen", TaskPriority.HIGH, "Commentar 3", true, false));
        taskliste = taskliste.stream()
                .filter(t -> !t.isFinalized())
                .collect(Collectors.toList());
        lvAusstehendeTasks.setItems(FXCollections.observableArrayList(taskliste));
        lvAusstehendeTasks.getSelectionModel().selectFirst();
    }

    private void initDateTimeline() {
        // Datum
        date = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            lblDate.setText(dateFormatterLong.format(LocalDate.now()));
        }), new KeyFrame(Duration.hours(24)));
        date.setCycleCount(Animation.INDEFINITE);
        date.play();
    }

    private void initTimeTimeline() {
        // Uhrzeit
        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            lblTime.setText(timeFormatter.format(LocalDateTime.now()));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void setUpTaskM() {
        List<Task> taskliste = new ArrayList<>();
        taskliste.add(new Task("Task 1", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, "Fixen", TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 2", LocalDateTime.now(), LocalDateTime.now().plusDays(4), null, "Priorisieren", TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, "Aufsetzen", TaskPriority.HIGH, "Commentar 3", true, false));

        ObservableList<Task> tasks = FXCollections.observableArrayList(taskliste);
        lvTaskM.setItems(tasks);
        lvTaskM.getSelectionModel().selectedItemProperty().addListener(listener -> {
            Task task = lvTaskM.getSelectionModel().getSelectedItem();
            tfTaskD.setText(task.getTitle());
            tfKategorieD.setText(task.getCategory());
            dateVon.setValue(task.getBeginning().toLocalDate());
            dateBis.setValue(task.getEnd().toLocalDate());
            timeVon.setValue(task.getBeginning().toLocalTime());
            timeBis.setValue(task.getEnd().toLocalTime());
            changePriority(task.getPriority());
            cbDeleteable.setSelected(task.isDeletable());
            taComment.setText(task.getNote());
            
            tabPane.getSelectionModel().select(1);
        });
    }

    private void changePriority(TaskPriority priority) {
        
//        for (JFXCheckBox cb : checkboxes) {
//            if (cb.isSelected()) {
//                
//            }
//        }
        cbHoch.setSelected(false);
        cbMittel.setSelected(false);
        cbNiedrig.setSelected(false);
        
        Notifier.INSTANCE.notifyInfo("Info", "This is an info");
        
//        for (TaskPriority p : priority.values()) {
//            if (p.equals(priority)) {
//                
//            }
//        }

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
//        if (cbHoch.isArmed()) {
//            cbMittel.disarm();
//            cbNiedrig.disarm();
//        } else if (cbMittel.isArmed()) {
//            cbHoch.disarm();
//            cbNiedrig.disarm();
//        } else if (cbNiedrig.isArmed()) {
//            cbHoch.disarm();
//            cbMittel.disarm();
//        }

    }

    public Timeline getClock() {
        return clock;
    }

    public Timeline getDate() {
        return date;
    }

    public Timeline getAutologout() {
        return autologout;
    }
    
    
}

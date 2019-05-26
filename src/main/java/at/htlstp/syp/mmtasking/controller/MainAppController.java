/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.controller;

import at.htlstp.syp.mmtasking.db.MMTDAO;
import at.htlstp.syp.mmtasking.model.Appointment;
import at.htlstp.syp.mmtasking.model.Category;
import at.htlstp.syp.mmtasking.model.Location;
import at.htlstp.syp.mmtasking.model.Task;
import at.htlstp.syp.mmtasking.model.TaskPriority;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import eu.hansolo.enzo.notification.Notification.Notifier;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
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
    private ChoiceBox<Location> cbOrt;
    @FXML
    private Button btnAuswerten;
    @FXML
    private ChoiceBox<TaskPriority> cbPriority;
    @FXML
    private JFXTextField tfTaskD;
    @FXML
    private JFXDatePicker dateVonD;
    @FXML
    private JFXTimePicker timeVonD;
    @FXML
    private JFXDatePicker dateBisD;
    @FXML
    private JFXTimePicker timeBisD;
    @FXML
    private JFXTextArea taCommentD;
    @FXML
    private JFXCheckBox cbHoch;
    @FXML
    private JFXCheckBox cbMittel;
    @FXML
    private JFXCheckBox cbNiedrig;
    @FXML
    private JFXCheckBox cbDeleteableD;
    @FXML
    private JFXListView<Task> lvAusstehendeTasks;
    @FXML
    private JFXDatePicker dateVonAnalyse;
    @FXML
    private JFXDatePicker dateBisAnalyse;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnFinalize;
    @FXML
    private MenuItem menuTask;
    @FXML
    private MenuItem menuApp;
    @FXML
    private ChoiceBox<Location> chbPrefLoc;
    @FXML
    private ChoiceBox<Category> cbCategoryD;
    @FXML
    private ChoiceBox<Location> cbLocs;

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("kk:mm:ss");
    private static final DateTimeFormatter dateFormatterLong = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy");
    private Timeline clock;
    private Timeline date;
    private Timeline autologout;
    private Instant logoutTime;
    private List<JFXCheckBox> checkboxes;
    private MMTDAO dao = MMTDAO.getInstance();
    
    private ObservableList<Category> categories;
    private ObservableList<Location> locations;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDateTime current;

        lblCurrentUser.setText("Current User: Alexandra Meinhard");

        initTimeTimeline();
        initDateTimeline();

        initFinalizing();

        setUpEnv();
        btnFinalize.setOnAction((ActionEvent e) -> {
            Task t = lvAusstehendeTasks.getSelectionModel().getSelectedItem();
            t.finalizeTask();
            lvAusstehendeTasks.getItems().remove(t);
        });

        btnEdit.setOnAction(e -> {
            Task task = lvAusstehendeTasks.getSelectionModel().getSelectedItem();
            tfTaskD.setText(task.getTitle());
            //cbCategory.getItems().contains(task.getCategory());
            //cbCategory.setSelectionModel();
            dateVonD.setValue(task.getBeginning().toLocalDate());
            dateBisD.setValue(task.getEnd().toLocalDate());
            timeVonD.setValue(task.getBeginning().toLocalTime());
            timeBisD.setValue(task.getEnd().toLocalTime());
            changePriority(task.getPriority());
            cbDeleteableD.setSelected(task.isDeletable());
            taCommentD.setText(task.getNote());

            tabPane.getSelectionModel().select(1);
        });

        setupAnalyse();

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

        setUpCurrentTasks();

        lvTerminM.setItems(appointments);
        
        System.out.println(dao.getAllCategories());
    }

    private void initFinalizing() {
        List<Task> taskliste = new ArrayList<>();
        taskliste.add(new Task("Task 1", LocalDateTime.now(), LocalDateTime.now().plusDays(5), "Fixen", TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 2", LocalDateTime.now(), LocalDateTime.now().plusDays(4), "Priorisieren", TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), "Aufsetzen", TaskPriority.HIGH, "Commentar 3", true, false));
        taskliste.add(new Task("Task 4", LocalDateTime.now(), LocalDateTime.now().plusDays(5), "Fixen", TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 5", LocalDateTime.now(), LocalDateTime.now().plusDays(4), "Priorisieren", TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 6", LocalDateTime.now(), LocalDateTime.now().plusDays(3), "Aufsetzen", TaskPriority.HIGH, "Commentar 3", true, false));
        taskliste.add(new Task("Task 7", LocalDateTime.now(), LocalDateTime.now().plusDays(5), "Fixen", TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 8", LocalDateTime.now(), LocalDateTime.now().plusDays(4), "Priorisieren", TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 9", LocalDateTime.now(), LocalDateTime.now().plusDays(3), "Aufsetzen", TaskPriority.HIGH, "Commentar 3", true, false));
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

//    private InvalidationListener taskListener = (l) -> {
//        l.
//        Task task = lvTaskM.getSelectionModel().getSelectedItem();
//
//        tfTaskD.setText(task.getTitle());
//        tfKategorieD.setText(task.getCategory());
//        dateVon.setValue(task.getBeginning().toLocalDate());
//        dateBis.setValue(task.getEnd().toLocalDate());
//        timeVon.setValue(task.getBeginning().toLocalTime());
//        timeBis.setValue(task.getEnd().toLocalTime());
//        changePriority(task.getPriority());
//        cbDeleteable.setSelected(task.isDeletable());
//        taComment.setText(task.getNote());
//
//        tabPane.getSelectionModel().select(1);
//    }
    private void setUpCurrentTasks() {
        lvTaskM.getItems().addAll(dao.getAllTasks());
        lvTaskM.getSelectionModel().selectedItemProperty().addListener(listener -> {
            Task task = lvTaskM.getSelectionModel().getSelectedItem();
            
            tfTaskD.setText(task.getTitle());
            Category c = Category.getCategory(task.getCategory());
            cbCategoryD.getSelectionModel().select(c);
            dateVonD.setValue(task.getBeginning().toLocalDate());
            dateBisD.setValue(task.getEnd().toLocalDate());
            timeVonD.setValue(task.getBeginning().toLocalTime());
            timeBisD.setValue(task.getEnd().toLocalTime());
            changePriority(task.getPriority());
            cbDeleteableD.setSelected(task.isDeletable());
            taCommentD.setText(task.getNote());

            tabPane.getSelectionModel().select(1);
        });

        cbHoch.setOnMouseClicked((event) -> {
            changePriority(TaskPriority.HIGH);
        });

        cbMittel.setOnMouseClicked((event) -> {
            changePriority(TaskPriority.MEDIUM);
        });

        cbNiedrig.setOnMouseClicked((event) -> {
            changePriority(TaskPriority.LOW);
        });

    }

    private void changePriority(TaskPriority priority) {
        cbHoch.setSelected(false);
        cbMittel.setSelected(false);
        cbNiedrig.setSelected(false);

        Notifier.INSTANCE.notifyInfo("Info", "This is an info");
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

    public Timeline getClock() {
        return clock;
    }

    public Timeline getDate() {
        return date;
    }

    public Timeline getAutologout() {
        return autologout;
    }

    public void setupCategoryDropdown() {
        cbKategorie.getItems().addAll(dao.getCategoriesforAnalyse());
    }

    public void setupLocationDropdown() {
        cbOrt.getItems().addAll(dao.getLocationsforAnalyse());
    }

    public void setupPriorityDropdown() {
        ObservableList<TaskPriority> cat = FXCollections.observableArrayList();
        cat.add(TaskPriority.HIGH);
        cat.add(TaskPriority.MEDIUM);
        cat.add(TaskPriority.LOW);
        cbPriority.setItems(cat);
    }

    private void setupAnalyse() {
        setupCategoryDropdown();
        setupLocationDropdown();
        setupPriorityDropdown();
    }

    @FXML
    private void openTaskDialog(ActionEvent event) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/AddTask.fxml"));
        } catch (IOException ex) {
            System.err.println("Task Dialog fail");
            //Logger.getLogger(this.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openAppDialog(ActionEvent event) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/AddApp.fxml"));
        } catch (IOException ex) {
            System.err.println("Appointment Dialog fail");
            //Logger.getLogger(this.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    private void setUpEnv() {
//        categories = FXCollections.observableArrayList(dao.getAllCategories());
//        locations = FXCollections.observableArrayList(dao.getAllLocations());
        cbLocs.getItems().addAll(dao.getAllLocations());
        cbCategoryD.getItems().addAll(dao.getAllCategories());
    }

    @FXML
    private void helpClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("MMT Solutions - NO RIGHTS RESERVED");
        alert.setTitle("Help");
        alert.show();

    }
}

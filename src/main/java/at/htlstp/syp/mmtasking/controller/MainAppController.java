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
    private JFXDatePicker dateVon;
    @FXML
    private JFXTimePicker timeVon;
    @FXML
    private JFXDatePicker dateBis;
    @FXML
    private JFXTimePicker timeBis;
    @FXML
    private JFXTextArea taComment;
    @FXML
    private JFXCheckBox cbHoch;
    @FXML
    private JFXCheckBox cbMittel;
    @FXML
    private JFXCheckBox cbNiedrig;
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

    private List<JFXCheckBox> checkboxes;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnFinalize;
    
    MMTDAO dao = new MMTDAO();
    @FXML
    private MenuItem menuTask;
    @FXML
    private MenuItem menuApp;
    @FXML
    private ChoiceBox<Location> chbPrefLoc;
    @FXML
    private ChoiceBox<Category> cbCategory;
    @FXML
    private ChoiceBox<Location> cbLocs;
    
    MMTDAO dao = new MMTDAO();
    @FXML
    private MenuItem menuTask;
    @FXML
    private MenuItem menuApp;
    @FXML
    private ChoiceBox<Location> chbPrefLoc;
    @FXML
    private ChoiceBox<Category> cbCategory;
    @FXML
    private ChoiceBox<Location> cbLocs;
    

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
            dateVon.setValue(task.getBeginning().toLocalDate());
            dateBis.setValue(task.getEnd().toLocalDate());
            timeVon.setValue(task.getBeginning().toLocalTime());
            timeBis.setValue(task.getEnd().toLocalTime());
            changePriority(task.getPriority());
            cbDeleteable.setSelected(task.isDeletable());
            taComment.setText(task.getNote());

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

        setUpTaskM();

        lvTerminM.setItems(appointments);
    }

    private void initFinalizing() {
        List<Task> taskliste = new ArrayList<>();
        taskliste.add(new Task("Task 1", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, new Category("Fixen"), TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 2", LocalDateTime.now(), LocalDateTime.now().plusDays(4), null, new Category("Priorisieren"), TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, new Category("Aufsetzen"), TaskPriority.HIGH, "Commentar 3", true, false));
        taskliste.add(new Task("Task 4", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, new Category("Fixen"), TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 5", LocalDateTime.now(), LocalDateTime.now().plusDays(4), null, new Category("Priorisieren"), TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 6", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, new Category("Aufsetzen"), TaskPriority.HIGH, "Commentar 3", true, false));
        taskliste.add(new Task("Task 7", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, new Category("Fixen"), TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 8", LocalDateTime.now(), LocalDateTime.now().plusDays(4), null, new Category("Priorisieren"), TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 9", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, new Category("Aufsetzen"), TaskPriority.HIGH, "Commentar 3", true, false));
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

    private void setUpTaskM() {
        List<Task> taskliste = new ArrayList<>();
        taskliste.add(new Task("Task 1", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, new Category("Fixen"), TaskPriority.MEDIUM, "Commentar 1", false, false));
        taskliste.add(new Task("Task 2", LocalDateTime.now(), LocalDateTime.now().plusDays(4), null, new Category("Priorisieren"), TaskPriority.LOW, "Commentar 2", false, false));
        taskliste.add(new Task("Task 3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, new Category("Aufsetzen"), TaskPriority.HIGH, "Commentar 3", true, false));

        cbHoch.setOnMouseClicked((event) -> {
                    changePriority(TaskPriority.HIGH);
        });
        
        cbMittel.setOnMouseClicked((event) -> {
                    changePriority(TaskPriority.MEDIUM);
        });
        
        cbNiedrig.setOnMouseClicked((event) -> {
                    changePriority(TaskPriority.LOW);
        });
        
        ObservableList<Task> tasks = FXCollections.observableArrayList(taskliste);
        lvTaskM.setItems(tasks);
        lvTaskM.getSelectionModel().selectedItemProperty().addListener(listener -> {
            Task task = lvTaskM.getSelectionModel().getSelectedItem();
            
            tfTaskD.setText(task.getTitle());
            //tfKategorieD.setText(task.getCategory()); //JM: added toString
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
        
        cbHoch.setSelected(false);
        cbMittel.setSelected(false);
        cbNiedrig.setSelected(false);
        
        //Notifier.INSTANCE.notifyInfo("Info", "This is an info");

    private void changePriority(TaskPriority priority) {

//        for (JFXCheckBox cb : checkboxes) {
//            cb.setSelected(false);
//            if (cb.equals(trigger)) {
//                cb.setSelected(true);
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
    
    public void setupCategoryDropdown(){
        
        //ObservableList<Category> cat = FXCollections.observableArrayList(dao.getCategoriesforAnalyse());
        //cbKategorie.setItems(cat);
    }
    
    public void setupLocationDropdown(){
        
        //ObservableList<Location> cat = FXCollections.observableArrayList(dao.getLocationsforAnalyse());
        //cbOrt.setItems(cat);
    }
    
    public void setupPriorityDropdown(){
        
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

        ObservableList<Location> locs = FXCollections.observableArrayList();
        //locs.addAll(dao.getAllLocation());
        locs.add(new Location("Irnfritz"));
        cbLocs.setItems(locs);
        
        ObservableList<Category> cat = FXCollections.observableArrayList();
        //locs.addAll(dao.getAllCategories());
        cat.add(new Category("Test"));
        cbCategory.setItems(cat);
        
        

    }

    @FXML
    private void helpClicked(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("MMT Solutions - NO RIGHTS RESERVED");
        alert.setTitle("Help");
        alert.show();
        
        
    }
            
      
    
    
}

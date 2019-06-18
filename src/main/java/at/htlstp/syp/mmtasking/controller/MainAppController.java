/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.controller;

import at.htlstp.syp.mmtasking.db.MMTDAO;
import at.htlstp.syp.mmtasking.db.MMTDBException;
import at.htlstp.syp.mmtasking.model.Appointment;
import at.htlstp.syp.mmtasking.model.Category;
import at.htlstp.syp.mmtasking.model.Fahrt;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
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

    private ObservableList<Category> categories = FXCollections.observableArrayList();
    private ObservableList<Location> locations = FXCollections.observableArrayList();
    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private ChoiceBox<String> cbFilter;
    @FXML
    private PieChart pieChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDateTime current;

        lblCurrentUser.setText("Current User: Alexandra Meinhard");
        
        initTimeTimeline();
        initDateTimeline();
        
        initData();

        initFinalizing();

        setUpEnv();
        btnFinalize.setOnAction((ActionEvent e) -> {
            Task t = lvAusstehendeTasks.getSelectionModel().getSelectedItem();
            t.finalizeTask();
            try {
                dao.updateTask(t);
                Notifier.INSTANCE.notifySuccess("Erfolg!", "Task wurde finalisiert!");
            } catch (MMTDBException ex) {
                Notifier.INSTANCE.notifyError("Fehler!", "Task wurde nicht finalisiert!");
            }
            refreshTasks();
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

        setUpDetailView();
        
        lvTerminM.setItems(appointments);
        initFahrzeitBinding();
    }
    
    private void refreshTasks() {
        tasks.addAll(dao.getAllTasks());
    }
    
    private void refreshAppointments() {
        appointments.addAll(dao.getAllAppointments());
    }
    
    private void refreshCategories() {
         categories.addAll(dao.getAllCategories());
    }
    
    private void refreshLocations() {
        locations.addAll(dao.getAllLocations());
    }
    
    private void initData() {
        refreshTasks();
        refreshAppointments();
        refreshCategories();
        refreshLocations();
    }

    private void initFinalizing() {
        // Replace with new DAO method!!!
        lvAusstehendeTasks.getItems().addAll(dao.getAllTasks()
                .stream()
                .filter(t -> !t.isFinalized())
                .collect(Collectors.toList()));
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
    private void setUpDetailView() {
        lvTaskM.setItems(tasks);
        lvTaskM.getSelectionModel().selectedItemProperty().addListener(listener -> {
            Task task = lvTaskM.getSelectionModel().getSelectedItem();

            tfTaskD.setText(task.getTitle());
            Category c = Category.getCategory(task.getCategory());
            cbCategoryD.getSelectionModel().select(c);
            dateVonD.setValue(task.getBeginning().toLocalDate());
            dateBisD.setValue(task.getEnd().toLocalDate());
            timeVonD.setValue(task.getBeginning().toLocalTime());
            timeBisD.setValue(task.getEnd().toLocalTime());
            cbLocs.getSelectionModel().select(task.getFahrt().getNach());
//            lblFahrzeit.setText("Die Fahrzeit beträgt " + task.getFahrt().getFahrzeit() + " min");
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
        cbLocs.getSelectionModel().selectFirst();
        cbCategoryD.getItems().addAll(dao.getAllCategories());
        cbCategoryD.getSelectionModel().selectFirst();
    }

    @FXML
    private void helpClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("MMT Solutions - NO RIGHTS RESERVED");
        alert.setTitle("Help");
        alert.show();

    }

    @FXML
    private void handleUebernehmen(ActionEvent event) {
    }

    private void initFahrzeitBinding() {
        StringBinding sb = new StringBinding() {
            {
                super.bind(cbLocs.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected String computeValue() {
                Location loc = cbLocs.getSelectionModel().getSelectedItem();
                Fahrt f = dao.getFahrtNach(loc);
                return "Die Fahrzeit beträgt " + f.getFahrzeit() + " min";
            }
        };
        lblFahrzeit.textProperty().bind(sb);
    }

    private void setupAnalyse() {
        cbFilter.getItems().addAll("Category", "Location");
        cbFilter.getSelectionModel().selectFirst();
        dateVonAnalyse.setValue(LocalDate.now());
        dateBisAnalyse.setValue(LocalDate.now());
        cbFilter.getSelectionModel().selectedItemProperty().addListener((Observable observable) -> {
            analyse();
        });
        dateVonAnalyse.valueProperty().addListener((Observable observable) -> {
            analyse();
        });
        dateBisAnalyse.valueProperty().addListener((Observable observable) -> {
            analyse();
        });

    }

    private void analyse() {
        barChart.getData().clear();
        LocalDateTime von;
        LocalDateTime bis;
        Double zeitInsgesamt = 0.0;
        List<Task> tasks = null;
        Map<String, Double> mapTaskTime = new LinkedHashMap<>();;
        List<PieChart.Data> listPie = new LinkedList<>();        //PieChart
        List<XYChart.Series> serien = new LinkedList<>();       //BarChart
        XYChart.Series series;    //BarChart

        if (dateVonAnalyse.getValue() == null) {
            von = LocalDate.now().atStartOfDay();
            dateVonAnalyse.setValue(LocalDate.now());
        } else {
            von = dateVonAnalyse.getValue().atStartOfDay();
        }

        if (dateBisAnalyse.getValue() == null) {
            bis = LocalDate.now().plusDays(1).atStartOfDay();
            dateBisAnalyse.setValue(LocalDate.now());
        } else {
            bis = dateBisAnalyse.getValue().plusDays(1).atStartOfDay();
        }

        try {
            tasks = MMTDAO.getInstance().getTasksByPeriod(von, bis);
        } catch (MMTDBException ex) {
            Logger.getLogger(MainAppController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (cbFilter.getSelectionModel().getSelectedItem().equals("Category")) {
            for (Task task : tasks) {                                                                                       // Map erstellen, von der Kategorie mit der dazugehörigen Zeit
                if (mapTaskTime.get(task.getCategory()) == null) {
                    mapTaskTime.put(task.getCategory(), 0.0);
                }
                mapTaskTime.put(task.getCategory(), mapTaskTime.get(task.getCategory()) + (task.getTime() / 60));
                zeitInsgesamt += task.getTime() / 60;
            }

            for (String cat : mapTaskTime.keySet()) {

                series = new XYChart.Series();

                listPie.add(new PieChart.Data(cat, Math.round(zeitInsgesamt / mapTaskTime.get(cat))));                    //Pie Chart mit Prozent Werten füllen
                series.getData().add(new XYChart.Data(cat, mapTaskTime.get(cat)));                                        //Bar Chart Serien erstellen 
                series.setName(cat);
                serien.add(series);
            }

        } else {
            for (Task task : tasks) {                                                                                // Map erstellen, von der Kategorie mit der dazugehörigen Zeit
                if (!mapTaskTime.containsKey(task.getFahrt().getNach().getName())) {
                    mapTaskTime.put(task.getFahrt().getNach().getName(), 0.0);
                }
                mapTaskTime.put(task.getFahrt().getNach().getName(), mapTaskTime.get(task.getFahrt().getNach().getName()) + task.getTime() / 60.0);
                zeitInsgesamt += task.getTime() / 60;
            }

            for (String location : mapTaskTime.keySet()) {

                series = new XYChart.Series();
                listPie.add(new PieChart.Data(location, Math.round((mapTaskTime.get(location) / zeitInsgesamt) * 100)));     //Pie Chart mit Prozent Werten füllen
                series.getData().add(new XYChart.Data(location, Math.round(mapTaskTime.get(location))));                      //Bar Chart Serien erstellen 
                series.setName(location);
                serien.add(series);

            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(listPie);
        pieChart.setData(pieChartData);
        pieChart.setLegendSide(Side.BOTTOM);
        serien.stream().forEach(s -> barChart.getData().addAll(s));

    }
}

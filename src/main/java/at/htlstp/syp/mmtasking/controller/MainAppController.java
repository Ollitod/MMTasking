/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.controller;

import at.htlstp.syp.mmtasking.MainApp;
import at.htlstp.syp.mmtasking.Setup;
import at.htlstp.syp.mmtasking.db.MMTDAO;
import at.htlstp.syp.mmtasking.db.MMTDBException;
import at.htlstp.syp.mmtasking.model.Category;
import at.htlstp.syp.mmtasking.model.Fahrt;
import at.htlstp.syp.mmtasking.model.Location;
import at.htlstp.syp.mmtasking.model.MMTUtil;
import at.htlstp.syp.mmtasking.model.Task;
import at.htlstp.syp.mmtasking.model.TaskPriority;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import eu.hansolo.enzo.notification.Notification.Notifier;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author josi
 */
public class MainAppController implements Initializable {

    @FXML
    private Label lblDate;
    @FXML
    private ListView<Task> lvTaskM;
    @FXML
    private Label lblTime;
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
    private MenuItem menuTask;
//    @FXML
//    private MenuItem menuApp;
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
    private MMTDAO dao = MMTDAO.getInstance();

    private ObservableList<Category> categories = FXCollections.observableArrayList();
    private ObservableList<Location> locations = FXCollections.observableArrayList();
    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private ChoiceBox<String> cbFilter;
    @FXML
    private PieChart pieChart;

    private Task selectedTask;

    private MainApp mainApp;

    private DateTimeFormatter dtfFile = DateTimeFormatter.ofPattern("ddMMyyyy");
    private DateTimeFormatter dtfStandard = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    private JFXButton btnSaveAnalysis;

    private LocalDate beginPeriod;
    private LocalDate endPeriod;

    private FilteredList<Task> filteredTasks;

    @FXML
    private ChoiceBox<String> cbViewMode;

    private final ObservableList<String> viewModes = FXCollections.observableArrayList("Woche", "Monat", "Jahr", "Alle");
    
    private int fromTab;
    
    private final BooleanProperty detailViewEmptyProperty = new SimpleBooleanProperty(true);

    private BooleanProperty detailViewEmptyProperty() {
        return detailViewEmptyProperty;
    }
    
    @FXML
    private Label caption;
    @FXML
    private JFXButton btnBack;
    @FXML
    private ImageView ivStatusD;
    @FXML
    private Tab tabDetailView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Uhrzeit initialisieren
        initTimeTimeline();

        // Datum initialisieren
        initDateTimeline();

        // Daten aus DB laden
        initData();

        // Finalisierung initialiseren (verbessern!)
        initFinalizing();

        // Controls initialiseren
        initEnvironment();
        
        tabDetailView.disableProperty().bind(detailViewEmptyProperty);
        
        // Listener für aktuell selektierten Tab hinzufügen
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.getText().equals("Detailansicht") && !detailViewEmptyProperty.get()) {
                clearDetailView();
            } else {
                fromTab = tabPane.getTabs().indexOf(oldValue);
            }
        });
        
        // Analyse initialiseren
        setupAnalysis();

        // Logik für Detailansicht initialiseren
        initDetailView();

        // Binding für Fahrzeit in Detailansicht initialisierne
        initFahrzeitBinding();
        
        // Benutzerdefinierte ListCell für ListView festlegen
        lvTaskM.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                return new TaskListCell();
            }
        });

        // Benutzerdefinierte ListCell für ListView festlegen
        lvAusstehendeTasks.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                return new TaskListCell();
            }
        });
    }

    public void setMainApplication(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public long getNumberOfTasksToday() {
        return tasks.stream()
                .filter(t -> t.getBeginning().toLocalDate().equals(LocalDate.now()))
                .count();
    }

    private void refreshTasks() {
        tasks.clear();
        tasks.addAll(dao.getAllTasks());
    }

    private void refreshCategories() {
        categories.clear();
        categories.addAll(dao.getAllCategories());
    }

    private void refreshLocations() {
        locations.clear();
        locations.addAll(dao.getAllLocations());
    }

    private void initData() {
        refreshTasks();
        refreshCategories();
        refreshLocations();
    }

    private void initEnvironment() {
//        categories = FXCollections.observableArrayList(dao.getAllCategories());
//        locations = FXCollections.observableArrayList(dao.getAllLocations());
        cbLocs.setItems(locations);
        cbLocs.getSelectionModel().selectFirst();
        cbCategoryD.setItems(categories);
        cbCategoryD.getSelectionModel().selectFirst();
        cbViewMode.setItems(viewModes);
        cbViewMode.getSelectionModel().selectFirst();
        cbViewMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (newValue) {
                    case "Woche":
                        changeViewOfTaskListView(ChronoUnit.WEEKS);
                        break;
                    case "Monat":
                        changeViewOfTaskListView(ChronoUnit.MONTHS);
                        break;
                    case "Jahr":
                        changeViewOfTaskListView(ChronoUnit.YEARS);
                        break;
                    case "Alle":
                        changeViewOfTaskListView(null);
                        break;
                    default:
                        break;
                }
            }
        });

        lvTaskM.setPlaceholder(new Label("Keine Tasks im ausgewählten Zeitraum!"));
        this.changeViewOfTaskListView(ChronoUnit.WEEKS);
        lvTaskM.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tasks.addListener((Observable obs) -> {
            initFinalizing();
        });
    }

    private void changeViewOfTaskListView(ChronoUnit unit) {
        if (unit == null) {
            lvTaskM.setItems(tasks);
        } else {
            switch (unit) {
                case WEEKS:
                    beginPeriod = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    endPeriod = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                    break;
                case MONTHS:
                    beginPeriod = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
                    endPeriod = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                    break;
                case YEARS:
                    beginPeriod = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
                    endPeriod = LocalDate.now().with(TemporalAdjusters.lastDayOfYear());
                    break;
                default:
                    throw new IllegalArgumentException("Tasks can only displayed per week, month, year or all tasks!");
            }
            filteredTasks = tasks.filtered(t -> MMTUtil.isDateBetweenInclusive(t.getBeginning().toLocalDate(), beginPeriod, endPeriod)
                    || MMTUtil.isDateBetweenInclusive(t.getEnd().toLocalDate(), beginPeriod, endPeriod));
            lvTaskM.setItems(filteredTasks);
        }
    }

    private void initFinalizing() {
        lvAusstehendeTasks.getItems().clear();
        lvAusstehendeTasks.getItems().addAll(dao.getAusstehendeTasks());
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

    private void initDetailView() {
        lvTaskM.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                selectedTask = lvTaskM.getSelectionModel().getSelectedItem();
                setDetailView();
            }
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

    private void setDetailView() {
        tfTaskD.setText(selectedTask.getTitle());
        Category c = Category.getCategory(selectedTask.getCategory());
        cbCategoryD.getSelectionModel().select(c);
        dateVonD.setValue(selectedTask.getBeginning().toLocalDate());
        dateBisD.setValue(selectedTask.getEnd().toLocalDate());
        timeVonD.setValue(selectedTask.getBeginning().toLocalTime());
        timeBisD.setValue(selectedTask.getEnd().toLocalTime());
        cbLocs.getSelectionModel().select(selectedTask.getFahrt().getNach());
        ivStatusD.setImage(selectedTask.isFinalized() ? new Image("success.png") : new Image("loading.png"));
        changePriority(selectedTask.getPriority());
        cbDeleteableD.setSelected(selectedTask.isDeletable());
        taCommentD.setText(selectedTask.getNote());

        detailViewEmptyProperty.set(false);
        tabPane.getSelectionModel().select(tabDetailView);
    }

    private void changePriority(TaskPriority priority) {
        cbHoch.setSelected(false);
        cbMittel.setSelected(false);
        cbNiedrig.setSelected(false);
        if (priority != null) {
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
        AddTaskController ctrl = null;
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/AddTask.fxml"));
            root = loader.load();
            ctrl = loader.getController();
            ctrl.setDialogStage(stage);
        } catch (IOException ex) {
            Notifier.INSTANCE.notifyError("Fehler", "Dialog konnte nicht geöffnet werden!");
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Task hinzufügen");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainApp.getStage().getScene().getWindow());
        stage.showAndWait();
        if (ctrl.getTask() != null) {
            tasks.add(ctrl.getTask());
        }
    }

//    @FXML
//    private void openAppDialog(ActionEvent event) {
//        Stage stage = new Stage();
//        Parent root = null;
//        try {
//            root = FXMLLoader.load(getClass().getResource("/AddApp.fxml"));
//        } catch (IOException ex) {
//            System.err.println("Appointment Dialog fail");
//            //Logger.getLogger(this.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//
//    }

    @FXML
    private void helpClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("MMT Solutions - NO RIGHTS RESERVED");
        alert.setTitle("Help");
        alert.show();

    }

    @FXML
    private void handleUebernehmen(ActionEvent event) {
        selectedTask.setTitle(tfTaskD.getText().trim());
        selectedTask.setCategory(cbCategoryD.getSelectionModel().getSelectedItem().getCatBez());
        selectedTask.setBeginning(LocalDateTime.of(dateVonD.getValue(), timeVonD.getValue()));
        selectedTask.setEnd(LocalDateTime.of(dateBisD.getValue(), timeBisD.getValue()));
        selectedTask.setFahrt(dao.getFahrtNach(cbLocs.getValue()));

        TaskPriority p = null;
        if (cbHoch.isSelected()) {
            p = TaskPriority.HIGH;
        } else if (cbMittel.isSelected()) {
            p = TaskPriority.MEDIUM;
        } else if (cbNiedrig.isSelected()) {
            p = TaskPriority.LOW;
        }
        selectedTask.setPriority(p);
        selectedTask.setDeletable(cbDeleteableD.isSelected());
        selectedTask.setNote(taCommentD.getText().trim());

        try {
            // Task in DB updaten
            dao.updateTask(selectedTask);

            // Task in Programm updaten
            tasks.set(tasks.indexOf(selectedTask), selectedTask);
            Notifier.INSTANCE.notifySuccess("Task upgedated!", "Der Task wurde erfolgreich upgedated!");
            tabPane.getSelectionModel().select(fromTab);
        } catch (MMTDBException ex) {
            Notifier.INSTANCE.notifyError("Fehlgeschlagen!", "Task wurde nicht upgedated!");
        }
    }

    private void clearDetailView() {
        tfTaskD.setText(null);
        cbCategoryD.getSelectionModel().selectFirst();
        dateVonD.setValue(null);
        dateBisD.setValue(null);
        timeVonD.setValue(null);
        timeBisD.setValue(null);
        cbLocs.getSelectionModel().selectFirst();
        ivStatusD.setImage(null);
        changePriority(null);
        cbDeleteableD.setSelected(false);
        taCommentD.setText(null);
        
        detailViewEmptyProperty.set(true);
        System.out.println("Detailview cleared!");
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
                return f.getFahrzeit() + " min";
            }
        };
        lblFahrzeit.textProperty().bind(sb);
    }

    private void setupAnalysis() {
        pieChart.setLegendSide(Side.BOTTOM);
        cbFilter.getItems().addAll("Category", "Location");
        cbFilter.getSelectionModel().selectFirst();
        dateVonAnalyse.setValue(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()));
        dateBisAnalyse.setValue(LocalDate.now());

        cbFilter.getSelectionModel().selectedItemProperty().addListener((Observable observable) -> {
            performAnalysis();
        });
        dateVonAnalyse.valueProperty().addListener((Observable observable) -> {
            performAnalysis();
        });
        dateBisAnalyse.valueProperty().addListener((Observable observable) -> {
            performAnalysis();
        });
        performAnalysis();
    }

    private void performAnalysis() {
        Double zeitInsgesamt = 0.0;
        List<Task> tasksToAnalyse = null;
        Map<String, Double> mapTaskTime = new LinkedHashMap<>();;
        List<PieChart.Data> pieChartDataList = new LinkedList<>();        //PieChart
        List<XYChart.Series<String, Number>> barChartDataList = new LinkedList<>();       //BarChart
        XYChart.Series<String, Number> barChartSeries;    //BarChart

        // Von
        if (dateVonAnalyse.getValue() == null) {
            dateVonAnalyse.setValue(LocalDate.now());
        }
        LocalDateTime von = dateVonAnalyse.getValue().atStartOfDay();

        // Bis
        if (dateBisAnalyse.getValue() == null) {
            dateBisAnalyse.setValue(LocalDate.now().plusDays(1));
        }
        LocalDateTime bis = dateBisAnalyse.getValue().atStartOfDay();

        try {
            tasksToAnalyse = dao.getTasksByPeriod(von, bis);
        } catch (MMTDBException ex) {
            Notifier.INSTANCE.notifyError("Fehler!", ex.getMessage());
        }

        // Daten aus Bar Chart loeschen
        barChart.getData().clear();

        // Caption-Label überschreiben
        caption.setText("");

        // Nach Kategorie analysieren
        if (cbFilter.getSelectionModel().getSelectedItem().equals("Category")) {
            // Map erstellen, die zu jeder Kategorie die aufgewandte Zeit speichert
            for (Task task : tasksToAnalyse) {
                if (mapTaskTime.get(task.getCategory()) == null) {
                    mapTaskTime.put(task.getCategory(), 0.0);
                }
                mapTaskTime.put(task.getCategory(), mapTaskTime.get(task.getCategory()) + (task.getTime() / 60.0));
                zeitInsgesamt += task.getTime() / 60.0;
            }

            for (String category : mapTaskTime.keySet()) {
                barChartSeries = new XYChart.Series();
                // Pie Chart mit Prozent Werten füllen
                pieChartDataList.add(new PieChart.Data(category, Math.round((mapTaskTime.get(category) / zeitInsgesamt) * 100)));
                // Bar Chart Serien erstellen
                XYChart.Data data = new XYChart.Data(category, mapTaskTime.get(category));
                barChartSeries.getData().add(data);
                barChartSeries.setName(category);
                barChartDataList.add(barChartSeries);
            }
        }

        // Nach Ort analysieren
        if (cbFilter.getSelectionModel().getSelectedItem().equals("Location")) {
            // Map erstellen, die zu jedem Ort die dort aufgewandte Zeit speichert
            for (Task task : tasksToAnalyse) {
                if (!mapTaskTime.containsKey(task.getFahrt().getNach().getName())) {
                    mapTaskTime.put(task.getFahrt().getNach().getName(), 0.0);
                }
                mapTaskTime.put(task.getFahrt().getNach().getName(), mapTaskTime.get(task.getFahrt().getNach().getName()) + task.getTime() / 60.0);
                zeitInsgesamt += task.getTime() / 60.0;
            }

            for (String location : mapTaskTime.keySet()) {
                barChartSeries = new XYChart.Series();
                // PieChart mit Prozentwerten füllen
                pieChartDataList.add(new PieChart.Data(location, Math.round((mapTaskTime.get(location) / zeitInsgesamt) * 100)));
                // BarChart Series erstellen
                XYChart.Data data = new XYChart.Data(location, Math.round(mapTaskTime.get(location)));
                barChartSeries.getData().add(data);
                barChartSeries.setName(location);
                barChartDataList.add(barChartSeries);
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(pieChartDataList);
        pieChart.getData().clear();
        pieChart.setData(pieChartData);

        // MouseClickListener für Prozentdarstellung auf PieChart
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    caption.setText(String.format("%.2f %% in %s" , data.getPieValue(), data.getName()));
                }
            });
        }


        barChart.getData().addAll(barChartDataList);

        // BarChart Clicklistener
        for (Series<String, Number> barChartSery : barChartDataList) {
            for (XYChart.Data<String, Number> data : barChartSery.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        caption.setText(String.format("%.2f h in %s", data.getYValue(), data.getXValue()));
                    }
                });
            }
        }
    }
    
    @FXML
    private void handleSaveAnalysisClicked(ActionEvent event) {
        printTaskEvaluationByCategoriesToFile(dateVonAnalyse.getValue(), dateBisAnalyse.getValue());
    }

    public void printTaskEvaluationByCategoriesToFile(LocalDate from, LocalDate to) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Paths.get(Setup.getDatabaseURL()).getParent().toFile());
        chooser.setInitialFileName("Task_Overview_" + from.format(dtfFile) + "_" + to.format(dtfFile) + ".txt");
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".txt", ".txt"));
        chooser.setTitle("Speichern unter...");

        File f = chooser.showSaveDialog(mainApp.getStage());
        if (f != null && !f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Notifier.INSTANCE.notifyError("Fehler beim erzeugen der Datei!", ex.getMessage());
            }
        }

        if (f != null) {
            try (PrintWriter pw = new PrintWriter(f)) {
                pw.println("Auswertung von " + from.format(dtfStandard) + " bis " + to.format(dtfStandard));

                // Gesamstunden ausrechnen
                double sum = tasks.stream()
                        .filter(t -> MMTUtil.isDateBetweenInclusive(t.getBeginning().toLocalDate(), from, to))
                        .mapToDouble(t -> t.getTime() / 60.0)
                        .sum();
                double onePercent = sum / 100;

                // Gruppieren nach Stunden
                TreeMap<String, Long> map = tasks.stream()
                        .filter(t -> MMTUtil.isDateBetweenInclusive(t.getBeginning().toLocalDate(), from, to))
                        .collect(Collectors.groupingBy(t -> t.getCategory(), TreeMap::new, Collectors.summingLong(t -> t.getTime())));

                // Gruppieren nach Stunden in %
                TreeMap<String, Double> map2 = tasks.stream()
                        .filter(t -> MMTUtil.isDateBetweenInclusive(t.getBeginning().toLocalDate(), from, to))
                        .collect(Collectors.groupingBy(t -> t.getCategory(), TreeMap::new, Collectors.summingDouble(t -> (t.getTime() / 60.0) / onePercent)));

                // In Datei schreiben
                pw.println("Gesamtaufwand in Stunden: " + String.format("%.2f", sum));
                pw.println();
                pw.println("Auswertung des Aufwands (in h):");
                map.forEach((category, time) -> pw.println(category + ": " + String.format("%.2f", time / 60.0)));
                pw.println();
                pw.println("Auswertung des Aufwands (in %):");
                map2.forEach((category, percent) -> pw.println(category + ": " + String.format("%.2f", percent)));
                pw.flush();

                Notifier.INSTANCE.notifySuccess("Erfolg!", "Auswertung erfolgreich unter '" + f.getAbsolutePath() + "' gespeichert!");
            } catch (FileNotFoundException fnfe) {
                Notifier.INSTANCE.notifyError("Fehler beim speichern!", fnfe.getMessage());
            }
        }
    }

    @FXML
    private void onQuitClicked(ActionEvent event) throws Exception {
        this.mainApp.stop();
    }

    @FXML
    private void onEditTaskClicked(ActionEvent event) {
        selectedTask = lvAusstehendeTasks.getSelectionModel().getSelectedItem();
        setDetailView();
    }

    @FXML
    private void onFinalizeTaskClicked(ActionEvent event) {
        if (!lvAusstehendeTasks.getItems().isEmpty()) {
            Task t = lvAusstehendeTasks.getSelectionModel().getSelectedItem();
            t.finalizeTask();
            try {
                dao.updateTask(t);
                tasks.set(tasks.indexOf(t), t);
                initFinalizing();
                Notifier.INSTANCE.notifySuccess("Erfolg!", "Task wurde finalisiert!");
            } catch (MMTDBException ex) {
                Notifier.INSTANCE.notifyError("Fehler!", "Task wurde nicht finalisiert!");
            }
        } else {
            Notifier.INSTANCE.notifyError("Fehler!", "Es sind keine Tasks zu finalisieren!");
        }
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        tabPane.getSelectionModel().select(fromTab);
    }
}

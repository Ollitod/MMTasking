/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.controller;

import at.htlstp.syp.mmtasking.MainApp;
import at.htlstp.syp.mmtasking.model.Task;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author 20150223
 */
public class TaskListCellController implements Initializable {

    @FXML
    private Label lblTitel;
    @FXML
    private Label lblVon;
    @FXML
    private Label lblBis;
    @FXML
    private Label lblOrt;
    @FXML
    private Label lblCategory;
    @FXML
    private ImageView ivStatus;
    
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm:ss");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setTask(Task task) {
        lblTitel.setText(task.getTitle());
        lblCategory.setText(task.getCategory());
        lblVon.setText("von " + task.getBeginning().format(DTF));
        lblBis.setText("bis  " + task.getEnd().format(DTF));
        lblOrt.setText(task.getFahrt().getNach().getName());
        ivStatus.setImage(task.isFinalized() ? new Image("success.png") : new Image("loading.png"));
    }
}

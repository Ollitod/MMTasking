/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.controller;

import at.htlstp.syp.mmtasking.MainApp;
import at.htlstp.syp.mmtasking.model.Task;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
public class TaskListCell extends ListCell<Task> {
    
    private static final String DEFAULT_CONTROL_INNER_BACKGROUND = "derive(-fx-base,80%)";

    @Override
    public void updateItem(Task item, boolean empty) {
        try {
            super.updateItem(item, empty);
            
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/TaskListCell.fxml"));
            Parent root = loader.load();
            TaskListCellController controller = loader.getController();
            if (item != null) {
                setGraphic(root);
                controller.setTask(item);
                switch (item.getPriority()) {
                    case LOW:
                        setStyle("-fx-control-inner-background: derive(#75ff66, 50%);");
                        break;
                    case MEDIUM:
                        setStyle("-fx-control-inner-background: derive(#ff9e49, 50%);");
                        break;
                    case HIGH:
                        setStyle("-fx-control-inner-background: derive(#ff6060, 50%);");
                        break;
                    default:
                        setStyle("-fx-control-inner-background: " + DEFAULT_CONTROL_INNER_BACKGROUND + ";");
                }
            } else {
                setGraphic(null);
                setStyle("-fx-control-inner-background: " + DEFAULT_CONTROL_INNER_BACKGROUND + ";");
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}

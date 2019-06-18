/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking;

import at.htlstp.syp.mmtasking.controller.MainAppController;
import at.htlstp.syp.mmtasking.db.JPAUtil;
import eu.hansolo.enzo.notification.Notification.Notifier;
import java.util.Arrays;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
public class MainApp extends Application {
    
    private MainAppController controller;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/MainApp.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setMainApplication(this);
        
        // Notifications konfigurieren
        Notifier.setPopupLocation(null, Pos.TOP_LEFT);
        Notifier.setNotificationOwner(stage);

        Scene scene = new Scene(root);

        this.stage = stage;
        stage.setScene(scene);
        stage.setTitle("MMTasking - © 2019 MMTSolutions");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        controller.getClock().stop();
        controller.getDate().stop();
        JPAUtil.close();
//        controller.getAutologout().stop();
    }
    
    public Stage getStage() {
        return stage;
    }
}

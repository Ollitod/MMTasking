/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking;

import at.htlstp.syp.mmtasking.controller.MainAppController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
public class MainApp extends Application {
    
    private MainAppController controller;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
//        System.out.println(getClass().getName());
//        System.out.println(getClass().getResource("view/MainApp.fxml").getPath());
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/MainApp.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);

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
        controller.getAutologout().stop();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Kris
 */
public class LMS extends Application {
    private static Parent root;
    private static Stage stage;
    private static String sessionUser;
    private static String registrationPassword;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static Parent getParent() {
        return root;
    }
    public static Stage getStage() {
        return stage;
    }
    
    public static void setSessionUser(String username) {
            sessionUser = username;
    }
    public static String getSessionUser() {
        return sessionUser;
    }
    public static String getRegistrationPassword() {
        return registrationPassword;
    }
    public static void setRegistrationPassword(String pass) {
        registrationPassword = pass;
    }
    
}

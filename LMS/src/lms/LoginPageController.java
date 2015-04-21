/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kris
 */
public class LoginPageController implements Initializable {
    
    @FXML
    TextField usernameInput;
    @FXML
    PasswordField passInput;
 
    private String password;
    private String username;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void loginEvent() {
        password = passInput.getText();
        username = usernameInput.getText();
        boolean matchFound = false;
        System.out.println("u: " + username + " p: " + password);
        Connection con = null;
        
        try {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
                "ayt2V3Ck");
        Statement stmt = con.createStatement();
        
        ResultSet users = stmt.executeQuery("SELECT * FROM USER");
        
        while (users.next() && !matchFound) {
            String u = users.getString("USERNAME");
            String p = users.getString("Password");
            //System.out.println(u + " " + p);
            if (username.equals(u) && password.equals(p)) {
                matchFound = true;
                System.out.println("Match found");
                goToHomePage();
            } 
        }
        
        if (!matchFound) {
            System.out.println("Match NOT found");
        }
        
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            try {
                if(con != null)
                con.close();
            } catch(SQLException e) {
                System.err.println(e);
            }   
        }    
    }
    
    private void goToHomePage() {
       try {
            LMS.setSessionUser(usernameInput.getText());
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("Home.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void registerEvent() {
       try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("RegistrationForm.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

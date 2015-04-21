/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kris
 */
public class RegistrationFormController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Button cancelButton;
    @FXML
    private Text error;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void continueRegistrationEvent(MouseEvent event) {
        String usernameInput = username.getText();
        String passwordInput = password.getText();
        String conPasswordInput = confirmPassword.getText();
        error.setText("");
        //Boolean pass = false;
        
        if (!passwordsEqual(passwordInput,conPasswordInput)) {
            confirmPassword.setText("");
            password.setText("");
            error.setText("Passwords must match!");
        } else if (usernameExists(usernameInput)) {
            error.setText("Username is already in the database");
        } else if (usernameInput.equals("")) {
            error.setText("Username can't be empty.");
        } else if (!usernameCheck(usernameInput)) {
            error.setText("Incorrect username format");
        } else if (!checkPassword(passwordInput)) {
            error.setText("Bad password format (length must be more than 3)");
        } else if (usernameInput.equals(passwordInput)) {
            error.setText("Username cannot match password");
        } else {
            LMS.setSessionUser(usernameInput);
            LMS.setRegistrationPassword(passwordInput);
            goToCreateProfile();
        }
    }
    
    private void goToCreateProfile() {
        try {
                Parent foster = LMS.getParent();
                Stage stage = LMS.getStage();
                foster = FXMLLoader.load(getClass().getResource("CreateProfilePage.fxml"));
        
                Scene scene = new Scene(foster);
        
                stage.setScene(scene);
                
                stage.show();    

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    @FXML
    private void cancelEvent(MouseEvent event) {
        LMS.setSessionUser(null);
        LMS.setRegistrationPassword(null);
        try {
                Parent foster = LMS.getParent();
                Stage stage = LMS.getStage();
                foster = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        
                Scene scene = new Scene(foster);
        
                stage.setScene(scene);
                stage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    private boolean usernameExists(String username) {
        boolean matchFound = false;
        Connection con = null;
        
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
                "ayt2V3Ck");
            Statement stmt = con.createStatement();

            ResultSet users = stmt.executeQuery("SELECT * FROM USER");
            //Export to function to stop the while loop with a return
            while (users.next() && !matchFound) {
                String u = users.getString("USERNAME");

                if (username.equals(u)) {
                    return true;
                } 

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
        return matchFound;
    }
    
    private boolean passwordsEqual(String pass1, String pass2) {
        return pass1.equals(pass2);
    }
    
    private boolean usernameCheck(String username) {
       return username.matches("[A-Za-z0-9]{3,16}");
    }
    
    private boolean checkPassword(String passwordInput) {
        return passwordInput.matches("[A-Za-z0-9\\s\\-\\_]{3,24}");
    }
}

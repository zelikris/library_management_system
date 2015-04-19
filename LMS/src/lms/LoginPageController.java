/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
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
    private void loginEvent(MouseEvent event) {
        password = passInput.getText();
        username = usernameInput.getText();
        boolean matchFound = false;
        System.out.println("u: " + username + " p: " + password);
        
        try { 
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        }
        
        try {
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/libraryDB", "kris", "zeli");
        Statement stmt = con.createStatement();
        
        ResultSet users = stmt.executeQuery("SELECT * FROM KRIS.\"USER\"");
        
        while (users.next() && !matchFound) {
            String u = users.getString("USERNAME");
            String p = users.getString("Password");
            //System.out.println(u + " " + p);
            if (username.equals(u) && password.equals(p)) {
                matchFound = true;
                System.out.println("Match found");
                goToSearchBooksPage();
            } 
        }
        
        if (!matchFound) {
            System.out.println("Match NOT found");
        }
        
        } catch(SQLException e) {
            System.err.println(e);
        }
    }
    
    private void goToSearchBooksPage() {
       try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("SearchBook.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void registerEvent(MouseEvent event) {
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

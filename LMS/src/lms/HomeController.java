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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kris
 */
public class HomeController implements Initializable {

    @FXML Button checkOutButton;
    @FXML Button returnButton;
    @FXML Button lostDamagedButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (userIsStaff()) {
            checkOutButton.setDisable(false);
            returnButton.setDisable(false);
            lostDamagedButton.setDisable(false);
        } else {
            checkOutButton.setDisable(true);
            returnButton.setDisable(true);
            lostDamagedButton.setDisable(true);
        }
    }    
    
    private Boolean userIsStaff() {
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");

            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM STAFF\n" +
                                                  "WHERE Staff_username = '" + LMS.getSessionUser() + "'");
            while (results.next()) {
                return true;
            }
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch(SQLException e) {
                System.err.println(e);
            }
        }
        return false;
    }
    
    @FXML
    private void onBackEvent() {
        LMS.setRegistrationPassword(null);
        LMS.setSessionUser(null);
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
    
    @FXML
    private void onSearchEvent(MouseEvent event) {
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
    private void onTrackEvent() {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("TrackBookLocation.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCheckOutEvent() {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("BookCheckout.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onHoldRequestEvent(MouseEvent event) {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("HoldRequestSearch.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onReturnBookEvent(MouseEvent event) {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("ReturnBookPage.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onFutureHoldRequestEvent(MouseEvent event) {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("FutureHoldRequest.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onRequestExtensionEvent(MouseEvent event) {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("RequestExtensionPage.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onViewReportsEvent(MouseEvent event) {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("ReportsPage.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onLostDamagedEvent(MouseEvent event) {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("LostDamagedBook.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

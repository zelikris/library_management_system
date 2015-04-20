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
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author keatts
 */
public class PenaltyPageController implements Initializable {

    @FXML Button lookUpButton;
    @FXML Button backButton;
    @FXML Button penaltyButton;
    @FXML TextField issueIdField;
    @FXML TextField userField;
    @FXML TextField dueDateField;
    @FXML TextField currentPenaltyField;
    @FXML TextField todaysDateField;
    @FXML TextField newPenaltyField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void backAction() {
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

    public void lookupAction() {
        Connection con = null;
        
        Integer issueId = Integer.parseInt(issueIdField.getText());

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");

            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT ISSUES.Issue_id, ISSUES.I_sf_username, ISSUES.Return_date, STUDENT_FACULTY.Penalty\n, CURDATE(), 0.50 * DATEDIFF(CURDATE(), T2.Return_date)" +
                                                  "FROM ISSUES\n" + 
                                                  "INNER JOIN STUDENT_FACULTY ON ISSUES.I_sf_username = STUDENT_FACULTY.Username\n" +
                                                  "WHERE ISSUES.Issue_id = '" + Integer.toString(issueId) + "'");

            while (results.next()) {
                String username = results.getString("I_sf_username");
                Date returnDate = results.getDate("Return_date");
                Double penalty = results.getDouble("Penalty");
                Date currentDate = results.getDate("CURDATE()");
                Double newPenalty = results.getDouble("0.50 * DATEDIFF(CURDATE(), T2.Return_date)");
                
                userField.setText(username);
                dueDateField.setText(returnDate.toString());
                currentPenaltyField.setText(Double.toString(penalty));
                todaysDateField.setText(currentDate.toString());
                newPenaltyField.setText(Double.toString(newPenalty));
                newPenaltyField.setEditable(true);
                newPenaltyField.setStyle("-fx-background-color: dddddd;");
                issueIdField.setEditable(false);

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

    }

    public void applyPenaltyAction() {
        Connection con = null;
        
        Integer issueId = Integer.parseInt(issueIdField.getText());
        Double newPenalty = Double.parseDouble(newPenaltyField.getText());

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");

            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("UPDATE STUDENT_FACULTY T1\n" +
                                                  "INNER JOIN (\n" +
                                                  "    SELECT I_sf_username, Penalty\n" +
                                                  "    FROM STUDENT_FACULTY \n" +
                                                  "    INNER JOIN ISSUES\n" +
                                                  "    ON STUDENT_FACULTY.Sf_username = ISSUES.I_sf_username\n" +
                                                  "    WHERE Issue_id = '" + issueId + "'\n" +
                                                  "    LIMIT 1\n" +
                                                  ") T2 ON T1.Sf_username = T2.I_sf_username\n" +
                                                  "SET T1.Penalty = " + newPenalty + ";\n" + 
                                                  "SELECT STUDENT_FACULTY.Penalty\n" +
                                                  "FROM ISSUES\n" + 
                                                  "INNER JOIN STUDENT_FACULTY ON ISSUES.I_sf_username = STUDENT_FACULTY.Username\n" +
                                                  "WHERE ISSUES.Issue_id = '" + Integer.toString(issueId) + "'");

            while (results.next()) {
                Double penalty = results.getDouble("Penalty");
                
                currentPenaltyField.setText(Double.toString(penalty));
                penaltyButton.setDisable(true);

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

    }
}

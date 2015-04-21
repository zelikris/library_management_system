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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author keatts
 */
public class ReturnBookPageController implements Initializable {

    @FXML Button backButton;
    @FXML Button returnButton;
    @FXML Button penaltyButton;
    @FXML ComboBox conditionBox;
    @FXML TextField issueIdField;
    @FXML TextField isbnField;
    @FXML TextField copyNoField;
    @FXML TextField usernameField;
    @FXML Text error;
    @FXML Text success;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conditionBox.getItems().addAll("N", "Y");
        conditionBox.setValue("N");
    }

    public void backAction() {
        try {
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

    public void penaltyAction() {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("PenaltyPage.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returnBookAction() {
        Connection con = null;
        error.setText("");
        success.setText("");
        if (issueIdField.getText().equals("")) {
            error.setText("IssueID can't be empty!");
        } else {
            Integer issueId = Integer.parseInt(issueIdField.getText());
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
                "ayt2V3Ck");

                Statement stmt = con.createStatement();
                ResultSet results = stmt.executeQuery("SELECT Issue_id, CURDATE() > Return_date\n" +
                                                      "FROM ISSUES\n" +
                                                      "WHERE Issue_id = '" + String.valueOf(issueId) + "'");
                
                while (results.next()) {
                    if (results.getString("Issue_id") == null) {
                        error.setText("Match NOT found");
                    } else {
                        Boolean isDamaged = conditionBox.getValue().equals("Y");
                        Boolean isLate = results.getBoolean("CURDATE() > Return_date");
                        System.out.println("Is book damaged? " + (isDamaged? "Y" : "N"));
                        returnBookOnTime(issueId, isDamaged);

                        if (isLate) {
                            error.setText("Book was returned late.  Assign penalty.");
                            penaltyButton.setDefaultButton(true);
                        } else {
                            success.setText("Returned!");
                        }
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
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

    public void returnBookOnTime(Integer issueId, Boolean isDamaged) {
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");

            Statement stmt = con.createStatement();
                stmt.executeUpdate("UPDATE BOOK_COPY T1\n" +
                                   "INNER JOIN (\n" +
                                   "    SELECT *\n" +
                                   "    FROM BOOK_COPY\n" +
                                   "    INNER JOIN ISSUES\n" +
                                   "    ON C_isbn = I_isbn AND Copy_number = I_copy_no\n" +
                                   "    WHERE Issue_id = '" + issueId + "'\n" +
                                   ") T2 ON T2.Issue_id = '" + issueId + "'\n" +
                                   "SET T1.Future_requester = NULL, T1.Is_checked_out = FALSE, T1.Is_damaged = '" + (isDamaged ? 1 : 0) + "'\n");

            ResultSet results = stmt.executeQuery("SELECT I_isbn, I_copy_no, I_sf_username\n" +
                                                  "FROM ISSUES\n" +
                                                  "WHERE Issue_id = '" + String.valueOf(issueId) + "'");

            while (results.next()) {
                String isbn = results.getString("I_isbn");
                Integer copyNo = results.getInt("I_copy_no");
                String username = results.getString("I_sf_username");

                isbnField.setText(isbn);
                copyNoField.setText(Integer.toString(copyNo));
                usernameField.setText(username);
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

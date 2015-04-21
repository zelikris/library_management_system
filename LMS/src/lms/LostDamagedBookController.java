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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
 * @author zain
 */
public class LostDamagedBookController implements Initializable {

    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button searchLastUser;
    @FXML
    private TextField isbn;
    @FXML
    private TextField bookCopyNumber;
    @FXML
    private TextField currentTime;
    @FXML
    private TextField lastUserOfBook;
    @FXML
    private TextField amountToCharge;
    @FXML
    private Button backButton;
    @FXML
    private Text error;
    @FXML
    private Text success;
    private boolean searchedUser;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentTime.setText(getTime());
    }    
    public void searchLastUserPressed(MouseEvent event) {
        success.setText("");
        String bookISBN = isbn.getText();
        String copyNum = bookCopyNumber.getText();
        if (bookISBN.equals("") || copyNum.equals("")) {
            error.setText("ISBN and Copy# can't be empty");
        } else {
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
                "ayt2V3Ck");

                Statement stmt = con.createStatement();
                ResultSet results = stmt.executeQuery("SELECT ISSUES.I_sf_username\n" +
                        "FROM ISSUES\n" +
                        "WHERE ISSUES.I_isbn = " + bookISBN + " AND ISSUES.I_copy_no = " + copyNum + "\n" +
                        "ORDER BY Date_of_issue DESC\n" +
                        "LIMIT 1");
                String lastUser = "";
                while (results.next()) {
                    lastUser = results.getString("I_sf_username");
                }
                if (lastUser.equals("")) {
                    error.setText("Invalid information");
                } else {
                    lastUserOfBook.setText(results.getString("I_sf_username"));
                    searchedUser = true;
                }
            } catch(Exception e) {
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
    
    public void submitButtonPressed(MouseEvent event) {
        success.setText("");
        String chargeThis = amountToCharge.getText();
        Connection con = null;
        if (!searchedUser) {
            error.setText("First Search for Last User");
        } else {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
                "ayt2V3Ck");

                Statement stmt = con.createStatement();
                stmt.executeUpdate("UPDATE STUDENT_FACULTY\n" +
                        "SET Penalty = Penalty + '" + chargeThis + "'\n" +
                        "WHERE Sf_username = '" + lastUserOfBook.getText() + "'");
                success.setText("Done!");
            } catch(Exception e) {
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
    
    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime()).toString();
    }
    
    public void backButtonPressed() {
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
    
    public void cancelButtonPressed() {
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
}

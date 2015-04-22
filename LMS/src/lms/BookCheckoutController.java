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
 * @author zain
 */
public class BookCheckoutController implements Initializable {

    @FXML
    private Button confirmButton;
    @FXML
    private TextField issueID;
    @FXML
    private TextField username;
    @FXML
    private TextField copyNumber;
    @FXML
    private TextField isbn;
    @FXML
    private TextField checkoutDate;
    @FXML
    private TextField estimatedReturnDate;
    @FXML
    private Button backButton;
    @FXML
    private Button prepareCheckoutButton;
    private boolean checkoutPrepared;
    @FXML
    private Text success = new Text();
    @FXML
    private Text error = new Text();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    public void prepareCheckoutButtonPressed(MouseEvent event) {
        success.setText("");
        String id = issueID.getText();
        if (id.equals("")) {
            error.setText("IssueID can't be empty!");
        } else {
            checkoutPrepared = true;
            error.setText("");
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
                "ayt2V3Ck");

                Statement stmt = con.createStatement();
                ResultSet results = stmt.executeQuery("SELECT I_sf_username, I_isbn, I_copy_no, Date_of_issue, DATE_ADD(CURDATE(), INTERVAL 14 DAY) AS Return_date, DATE_ADD(Date_of_issue, INTERVAL 3 DAY) <= CURDATE()\n" +
                        "FROM ISSUES\n" +
                        "WHERE Issue_id = " + id + "\n");
                String userName = "";
                String copyNo = "";
                String isbnNo = "";
                String checkout = "";
                String estReturn = "";
                while (results.next()) {
                    if (Integer.parseInt(results.getString("DATE_ADD(Date_of_issue, INTERVAL 3 DAY) <= CURDATE()")) > 0) {
                        userName = results.getString("I_sf_username");
                        copyNo = results.getString("I_copy_no");
                        isbnNo = results.getString("I_isbn");
                        checkout = results.getString("Date_of_issue");
                        estReturn = results.getString("Return_date");
                    } else {
                        error.setText("Your hold has been dropped");
                        stmt.executeUpdate("UPDATE BOOK_COPY\n" +
                                "INNER JOIN (\n" +
                                "SELECT C_isbn, Copy_number, Return_date, Future_requester, Is_checked_out, Is_on_hold\n" +
                                "FROM BOOK_COPY\n" +
                                "INNER JOIN ISSUES\n" +
                                "ON BOOK_COPY.C_isbn = ISSUES.I_isbn AND BOOK_COPY.Copy_number = I_copy_no\n" +
                                "WHERE ISSUES.Issue_id = " + id + "\n" +
                                ") T2 ON T1.C_isbn = T2.C_isbn AND T1.Copy_number = T2.Copy_number\n" +
                                "SET T1.Is_on_hold = FALSE");
                        checkoutPrepared = false;
                    }
                }
                if (userName.equals("")) {
                    error.setText("Invalid ISBN!");
                    checkoutPrepared = false;
                } else {
                    username.setText(userName);
                    copyNumber.setText(copyNo);
                    isbn.setText(isbnNo);
                    checkoutDate.setText(checkout);
                    estimatedReturnDate.setText(estReturn);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }
    @FXML
    public void confirmButtonPressed(MouseEvent event) {
        success.setText("");
        if (!checkoutPrepared) {
            error.setText("First Prepare Checkout");
        } else {
            error.setText((""));
            Connection con = null;
            String id = issueID.getText();
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
                "ayt2V3Ck");

                Statement stmt = con.createStatement();
                stmt.executeUpdate("UPDATE BOOK_COPY T1\n" +
                        "INNER JOIN (\n" +
                        "SELECT C_isbn, Copy_number, Return_date, Future_requester, Is_checked_out, Is_on_hold\n" +
                        "FROM BOOK_COPY\n" +
                        "INNER JOIN ISSUES\n" +
                        "ON BOOK_COPY.C_isbn = ISSUES.I_isbn AND BOOK_COPY.Copy_number = I_copy_no\n" +
                        "WHERE ISSUES.Issue_id = " + id + "\n" +
                        ") T2 ON T1.C_isbn = T2.C_isbn AND T1.Copy_number = T2.Copy_number\n" +
                        "SET T1.Is_checked_out = TRUE, T1.Is_on_hold = FALSE");
                stmt.executeUpdate("UPDATE ISSUES\n" +
                        "SET Return_date = DATE_ADD(CURDATE(), INTERVAL 14 DAY), Extension_date = CURDATE()" +
                        "WHERE Issue_id = " + id);
                success.setText("Done!");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
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
}

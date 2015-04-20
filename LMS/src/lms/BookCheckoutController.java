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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    public void confirmButtonPressed(MouseEvent event) {
        String id = issueID.getText();
        if (id.equals("")) {
            System.out.println("Issue ID can't be empty!");
        }
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");

            Statement stmt = con.createStatement();
            ResultSet results =  stmt.executeQuery("SELECT BOOK.COPY.C_isbn, BOOK_COPY.Copy_number\n" + 
                    "FROM BOOK_COPY\n" + 
                    "WHERE BOOK_COPY.C_isbn = " );
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

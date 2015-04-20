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
public class TrackBookLocationController implements Initializable {

    @FXML
    private TextField isbn;
    @FXML
    private Button locateButton;
    @FXML
    private TextField floorNumber;
    @FXML
    private TextField aisleNumber;
    @FXML
    private TextField subjectName;
    @FXML
    private TextField shelfNumber;
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
    public void locateButtonPressed(MouseEvent event) {
        String bookISBN = isbn.getText();
        if (bookISBN.equals("")) {
            System.out.println("Book ISBN can't be empty!");
        }
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
                "ayt2V3Ck");
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT BOOK.Isbn, BOOK.Subject_name, BOOK.Shelf_no, SHELF.Shelf_number, SHELF.Aisle_number,\n" +
                    "SHELF.Floor_no\n" +
                    "FROM BOOK\n" +
                    "INNER JOIN SHELF\n" +
                    "ON BOOK.Shelf_no = SHELF.Shelf_number\n" +
                    "WHERE BOOK.Isbn = " + isbn.getText() + "\n");
            String floorNo = "";
            String aisleNo = "";
            String subject = "";
            String shelfNo = "";
            while (results.next()) {
                floorNo = results.getString("Floor_no");
                aisleNo = results.getString("Aisle_number");
                subject = results.getString("Subject_name");
                shelfNo = results.getString("Shelf_no");
            }
            floorNumber.setText(floorNo);
            aisleNumber.setText(aisleNo);
            shelfNumber.setText(shelfNo);
            subjectName.setText(subject);
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
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
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

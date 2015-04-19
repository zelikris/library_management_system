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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author keatts
 */
public class HoldRequestPageController implements Initializable {

    @FXML 
    private TextField holdRequestDateField;
    @FXML 
    private TextField returnDateField;
    @FXML
    private ComboBox<Book> selectAvail;
    
    @FXML
    private TableView<Book> availableTable;
    @FXML
    private TableView<Book> onReserveTable;
    
    @FXML
    private TableColumn<Book, String> availIsbn;
    @FXML
    private TableColumn<Book, String> availTitle;
    @FXML
    private TableColumn<Book, String> availEdition;
    @FXML
    private TableColumn<Book, Integer> availNoAvailableCopies;
    
    @FXML
    private TableColumn<Book, String> resIsbn;
    @FXML
    private TableColumn<Book, String> resTitle;
    @FXML
    private TableColumn<Book, String> resEdition;
    @FXML
    private TableColumn<Book, Integer> resNoAvailableCopies;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Book> booksAvailable = HoldRequestSearchController.getAvailableBooks();
        ArrayList<Book> booksOnReserve = HoldRequestSearchController.getBooksOnReserve();
        
        ObservableList<Book> availableList = FXCollections.observableArrayList(booksAvailable);
        ObservableList<Book> reserveList = FXCollections.observableArrayList(booksOnReserve);
        
        availIsbn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        availTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        availEdition.setCellValueFactory(new PropertyValueFactory<Book, String>("edition")); 
        availNoAvailableCopies.setCellValueFactory(new PropertyValueFactory<Book, Integer>("copiesAvailable")); 
        
        resIsbn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        resTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        resEdition.setCellValueFactory(new PropertyValueFactory<Book, String>("edition")); 
        resNoAvailableCopies.setCellValueFactory(new PropertyValueFactory<Book, Integer>("copiesAvailable")); 
        
        availableTable.setItems(availableList);
        selectAvail.setItems(availableList);
        onReserveTable.setItems(reserveList);
        
        // hold request date = curdate
        SimpleDateFormat sFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date curDate = new Date();
        Date newDate = new Date();
        
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        c.add(Calendar.DATE, 17);
        newDate = c.getTime();

        String date = sFormat.format(new Date());
        holdRequestDateField.setText(date);
        
        // estimated return date = curdate + 17
        returnDateField.setText(sFormat.format(newDate));
    }    
    
    @FXML
    private void onSubmitEvent(MouseEvent event) {
        if (selectAvail.getValue() != null) {
            updateBookCopy();
        } else {
            System.out.println("No book was selected!");
        }
    }
    
    private void updateBookCopy() {
        Book comboSelection = selectAvail.getValue();
        System.out.println(comboSelection.toString());
        System.out.println("CurUser: " + LMS.getSessionUser());
        System.out.println("CopyNumber: "+ comboSelection.getCopyNumber());
        int issueID = 0;
        
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
           
            Statement stmt = con.createStatement();
            
            ResultSet maxIssueId = stmt.executeQuery("SELECT MAX( Issue_id ) + 1 FROM ISSUES");
            while (maxIssueId.next()) {
                issueID = maxIssueId.getInt("MAX( Issue_id ) + 1");
            }
            System.out.println("IssueID: " + issueID);
            stmt.executeUpdate("UPDATE BOOK_COPY\n" +
                            "SET Is_on_hold = TRUE, Is_checked_out = FALSE\n" +
                            "WHERE C_isbn = '" + comboSelection.getIsbn() + "' AND Copy_number = '" + comboSelection.getCopyNumber() + "'\n" +
                            "INSERT INTO ISSUES\n" +
                            "VALUES ('" + comboSelection.getIsbn() + "', " + comboSelection.getCopyNumber() + ", '" + LMS.getSessionUser() + "', DATE_ADD(CURDATE(), INTERVAL 17 DAY), DATE_ADD(CURDATE(), INTERVAL 17 DAY), '" + issueID + "')");
           
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
    
    @FXML
    private void onBackEvent(MouseEvent event)  {
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
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author zain
 */
public class FutureHoldRequestController implements Initializable {

    @FXML
    private TextField isbnInput;
    @FXML
    private TextField copyNumberOut;
    @FXML
    private TextField availableDate;
    @FXML
    private TextField user;
    @FXML
    private Text success;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    private void onRequestEvent(MouseEvent event) {
        if (!isbnInput.getText().equals("")) {
            searchDB();
        } else {
            System.out.println("ISBN cannot be null!");
        }
    }
    
    @FXML
    private void onOkEvent(MouseEvent event) {
        if (!isbnInput.getText().equals("") && !copyNumberOut.getText().equals("")) {
            updateDB();
            success.setVisible(true);
        } else {
            System.out.println("Cannot submit future hold on zero books!");
        }
    }
    
    private void updateDB() {
        String isbn = isbnInput.getText();
        String user = LMS.getSessionUser();
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
            Statement stmt = con.createStatement();

            stmt.executeUpdate("UPDATE BOOK_COPY T1\n" +
                                "INNER JOIN (\n" +
                                "    SELECT C_isbn, Copy_number, Return_date, Future_requester\n" +
                                "    FROM BOOK_COPY \n" +
                                "    INNER JOIN ISSUES\n" +
                                "    ON BOOK_COPY.C_isbn = ISSUES.I_isbn AND BOOK_COPY.Copy_number = I_copy_no\n" +
                                "    WHERE C_isbn = '" + isbn + "' AND BOOK_COPY.Copy_number = " + copyNumberOut.getText() + " AND Future_requester IS NULL\n" +
                                ") T2 ON T1.C_isbn = T2.C_isbn AND T1.Copy_number = T2.Copy_number\n" +
                                "SET T1.Future_Requester = '" + user + "';");  
                                                      
            } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
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
    
    private void searchDB() {
        String isbn = isbnInput.getText();
        //String user = LMS.getSessionUser();
        Connection con = null;
        Boolean matchFound = false;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
            
        Statement stmt = con.createStatement();
        
        ResultSet books = stmt.executeQuery("SELECT Copy_number, Return_date, Future_requester\n" +
                                            "FROM BOOK_COPY \n" +
                                            "INNER JOIN ISSUES\n" +
                                            "ON BOOK_COPY.C_isbn = ISSUES.I_isbn AND BOOK_COPY.Copy_number = I_copy_no\n" +
                                            "WHERE C_Isbn = '" + isbn + "' AND Future_requester IS NULL\n" +
                                            "ORDER BY Return_date DESC");     
                                          
        while (books.next() && !matchFound) {
            String copyNumber = books.getString("Copy_number");
            String returnDate = books.getString("Return_date");
            copyNumberOut.setText(copyNumber);
            availableDate.setText(returnDate);
            user.setText(LMS.getSessionUser());

            matchFound = true;
            System.out.println("Match found: copy#: " + copyNumber + ", return date: " + returnDate);
        }
        
        if (!matchFound) {
            System.out.println("Match NOT found");
        }
            
            } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
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
    
}

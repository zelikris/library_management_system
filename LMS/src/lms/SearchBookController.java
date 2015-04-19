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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kris
 */
public class SearchBookController implements Initializable {

    @FXML
    private TextField isbnInput;
    @FXML
    private TextField titleInput;
    @FXML
    private TextField authorInput;
    
    private String isbn;
    private String title;
    private String author;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void onBackEvent(MouseEvent event) {
        try {
            LMS.setSessionUser(null);
            LMS.setRegistrationPassword(null);
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
        if (!isbnInput.getText().equals("")) {
            searchByIsbn();
        } else if (!titleInput.getText().equals("")) {
            searchByTitle();
        } else if (!authorInput.getText().equals("")) {
            searchByAuthor();
        } else {
            System.out.println("All fields Null!");
        }
    }
    
    private void searchByIsbn() {
        isbn = isbnInput.getText();
        boolean matchFound = false;
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
           
            
            Statement stmt = con.createStatement();

            // TODO: if not available: show the date when the book will be available, which is based on last check-out OR hold-status
            ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve\n" +
                                                "FROM BOOK_COPY \n" +
                                                "INNER JOIN BOOK \n" +
                                                "ON BOOK_COPY.C_ISBN = BOOK.ISBN \n" +
                                                "INNER JOIN BOOK_AUTHORS \n" +
                                                "ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN \n" +
                                                "LEFT JOIN ISSUES \n" +
                                                "ON BOOK_COPY.C_isbn = ISSUES.I_Isbn AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                                "WHERE BOOK.ISBN = '" + isbn + "'");

            while (books.next() && !matchFound) {       
                matchFound = true;
                System.out.println("Match found");
            }

            if (!matchFound) {
                System.out.println("Match NOT found");
            }

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
    
    private void searchByTitle() {
        title = titleInput.getText();
        boolean matchFound = false;
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
           
            
            Statement stmt = con.createStatement();

            // TODO: if not available: show the date when the book will be available, which is based on last check-out OR hold-status
            ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve\n" +
                                                "FROM BOOK_COPY \n" +
                                                "INNER JOIN BOOK \n" +
                                                "ON BOOK_COPY.C_ISBN = BOOK.ISBN \n" +
                                                "INNER JOIN BOOK_AUTHORS \n" +
                                                "ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN \n" +
                                                "LEFT JOIN ISSUES \n" +
                                                "ON BOOK_COPY.C_isbn = ISSUES.I_Isbn AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                                "WHERE BOOK.Title = '" + title + "'");

            while (books.next() && !matchFound) {       
                matchFound = true;
                System.out.println("Match found");
            }

            if (!matchFound) {
                System.out.println("Match NOT found");
            }
            
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
    
    private void searchByAuthor() {
        author = authorInput.getText();
        boolean matchFound = false;
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
           
            
        Statement stmt = con.createStatement();
        
        // TODO: if not available: show the date when the book will be available, which is based on last check-out OR hold-status
        ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve\n" +
                                            "FROM BOOK_COPY \n" +
                                            "INNER JOIN BOOK \n" +
                                            "ON BOOK_COPY.C_ISBN = BOOK.ISBN \n" +
                                            "INNER JOIN BOOK_AUTHORS \n" +
                                            "ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN \n" +
                                            "LEFT JOIN ISSUES \n" +
                                            "ON BOOK_COPY.C_isbn = ISSUES.I_Isbn AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                            "WHERE BOOK_AUTHORS.Name = '" + author + "'");
                                          
        while (books.next() && !matchFound) {       
            matchFound = true;
            System.out.println("Match found");
        }
        
        if (!matchFound) {
            System.out.println("Match NOT found");
        }
            
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
}

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kris
 */
public class HoldRequestSearchController implements Initializable {

    @FXML
    private TextField isbnInput;
    @FXML
    private TextField titleInput;
    @FXML
    private TextField authorInput;   
    private String isbn;
    private String title;
    private String author;
    private static ArrayList<Book> availableBooks;
    private static ArrayList<Book> booksOnReserve;
    @FXML
    private Text error;
    private boolean matchFound = false;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        availableBooks = new ArrayList<Book>();
        booksOnReserve = new ArrayList<Book>();
    }
    
    @FXML
    private void onBackEvent(MouseEvent event) {
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
    
    @FXML
    private void onSearchEvent(MouseEvent event) {
        if (!isbnInput.getText().equals("")) {
            searchByIsbn();
            if (matchFound) {
               goToHoldRequestPage();
           } else {
               error.setText("Match not found!");
           }
        } else if (!titleInput.getText().equals("")) {
            searchByTitle();
           if (matchFound) {
               goToHoldRequestPage();
           } else {
               error.setText("Match not found!");
           }
        } else if (!authorInput.getText().equals("")) {
           searchByAuthor();
           if (matchFound) {
               goToHoldRequestPage();
           } else {
               error.setText("Match not found!");
           }
        } else {
            error.setText("All fields can't be empty.");
        }
    }
    
    private void goToHoldRequestPage() {
        try {
                Parent foster = LMS.getParent();
                Stage stage = LMS.getStage();
                foster = FXMLLoader.load(getClass().getResource("HoldRequestPage.fxml"));

                Scene scene = new Scene(foster);

                stage.setScene(scene);
                stage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    private void searchByIsbn() {
        isbn = isbnInput.getText();
        matchFound = false;
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
           
            
            Statement stmt = con.createStatement();

            // TODO: if not available: show the date when the book will be available, which is based on last check-out OR hold-status
            ResultSet books = stmt.executeQuery("SELECT BOOK_COPY.Copy_Number, BOOK.Isbn, BOOK.Title, BOOK.Edition, BOOK.Is_Book_On_Reserve, COUNT(BOOK_COPY.Copy_Number)\n" +
                                                "FROM BOOK_COPY \n" +
                                                "INNER JOIN BOOK \n" +
                                                "ON BOOK_COPY.C_ISBN = BOOK.ISBN \n" +                                                
                                                "WHERE BOOK.ISBN = '" + isbn + "' AND BOOK_COPY.Is_Checked_Out = 0 AND BOOK_COPY.Is_damaged = 0 AND BOOK_COPY.Is_On_Hold = 0");

            while (books.next()) { 
                int copyNumber = books.getInt("Copy_number");
                //System.out.println("Copy number: " + copyNumber);
                String theIsbn = books.getString("Isbn");
                String theTitle = books.getString("Title");
                String theEdition = books.getString("Edition");
                String onReserve = books.getString("Is_Book_On_Reserve");
                int copiesAvailable = books.getInt("COUNT(BOOK_COPY.Copy_Number)");        
                addBookToList(theIsbn, theTitle, theEdition, onReserve, copiesAvailable, copyNumber);  
                matchFound = true;
                System.out.println("Match found: " + books.getString("Title"));
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
        matchFound = false;
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
           
            
            Statement stmt = con.createStatement();

            // TODO: if not available: show the date when the book will be available, which is based on last check-out OR hold-status
            ResultSet books = stmt.executeQuery("SELECT BOOK_COPY.Copy_Number, BOOK.Isbn, BOOK.Title, BOOK.Edition, BOOK.Is_Book_On_Reserve, COUNT(BOOK_COPY.Copy_Number)\n" +
                                                "FROM BOOK_COPY \n" +
                                                "INNER JOIN BOOK \n" +
                                                "ON BOOK_COPY.C_ISBN = BOOK.ISBN \n" +         
                                                "WHERE BOOK.Title = '" + title + "' AND BOOK_COPY.Is_Checked_Out = 0 AND BOOK_COPY.Is_damaged = 0 AND BOOK_COPY.Is_On_Hold = 0\n" +
                                                "GROUP BY BOOK.Title");

            while (books.next()) { 
                int copyNumber = books.getInt("Copy_number");
                String theIsbn = books.getString("Isbn");
                String theTitle = books.getString("Title");
                String theEdition = books.getString("Edition");
                String onReserve = books.getString("Is_Book_On_Reserve");
                int copiesAvailable = books.getInt("COUNT(BOOK_COPY.Copy_Number)");
                
                addBookToList(theIsbn, theTitle, theEdition, onReserve, copiesAvailable, copyNumber);
                
                matchFound = true;
                System.out.println("Match found: " + books.getString("Title"));
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
        matchFound = false;
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");       
            Statement stmt = con.createStatement();

            // TODO: if not available: show the date when the book will be available, which is based on last check-out OR hold-status
            ResultSet books = stmt.executeQuery("SELECT BOOK_COPY.Copy_Number, BOOK.Isbn, BOOK.Title, BOOK.Edition, BOOK.Is_Book_On_Reserve, COUNT(BOOK_COPY.Copy_Number)\n" +
                                                "FROM BOOK_COPY \n" +
                                                "INNER JOIN BOOK \n" +
                                                "ON BOOK_COPY.C_ISBN = BOOK.ISBN \n" +
                                                "INNER JOIN BOOK_AUTHORS \n" +
                                                "ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN \n" +                                                
                                                "WHERE BOOK_AUTHORS.Name = '" + author + "' AND BOOK_COPY.Is_Checked_Out = 0 AND BOOK_COPY.Is_damaged = 0 AND BOOK_COPY.Is_On_Hold = 0\n" +
                                                "GROUP BY BOOK.Title");
            while (books.next()) { 
                int copyNumber = books.getInt("Copy_number");
                String theIsbn = books.getString("Isbn");
                String theTitle = books.getString("Title");
                String theEdition = books.getString("Edition");
                String onReserve = books.getString("Is_Book_On_Reserve");
                int copiesAvailable = books.getInt("COUNT(BOOK_COPY.Copy_Number)");
                
                addBookToList(theIsbn, theTitle, theEdition, onReserve, copiesAvailable, copyNumber);
                matchFound = true;
                System.out.println("Match found: " + books.getString("Title"));
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
    
    private void addBookToList(String theIsbn, String theTitle, String theEdition, String onReserve, int copiesAvailable, int copyNumber) {
        Boolean isCheckedOut = null;
        Boolean isOnReserve = null;
        if (onReserve.equalsIgnoreCase("1")) {
            isOnReserve = true;
        } else {
            isOnReserve = false;
        }
        Book b = new Book(theIsbn, theTitle, theEdition, isOnReserve, copiesAvailable, copyNumber);
        if (isOnReserve) {
            booksOnReserve.add(b);
        } else {
            if (copiesAvailable != 0) {
                availableBooks.add(b);
            }
        }
    }
    
    public static ArrayList<Book> getAvailableBooks() {
        return availableBooks;
    }
    
    public static ArrayList<Book> getBooksOnReserve() {
        return booksOnReserve;
    }
}

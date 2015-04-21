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
public class SearchBookController implements Initializable {

    @FXML
    private TextField isbnInput;
    @FXML
    private TextField titleInput;
    @FXML
    private TextField authorInput;
    @FXML
    private Text error;
    @FXML
    private TextField publisherInput;
    @FXML
    private TextField editionInput;   
    
    private String publisher;
    private String edition; 
    private String isbn;
    private String title;
    private String author;
    private static HashSet<Book> booksFound;
    boolean matchFound = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        booksFound = new HashSet<>();
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
        searchDB();
        if (allFieldsNull()) {
            error.setText("All fields can't be empty!");
        } else {
            if (matchFound) {
                goToShowBooksScreen();
            } else {
                error.setText("Match not Found!");
            }
        }
    }
    
    private Boolean allFieldsNull() {
        if (isbnInput.getText().equals("") && titleInput.getText().equals("") && authorInput.getText().equals("") && 
                publisherInput.getText().equals("") && editionInput.getText().equals("")) {
            return true;
        }
        return false;
    }
    
    private void searchDB() {
        isbn = isbnInput.getText();
        title = titleInput.getText();
        author = authorInput.getText();
        publisher = publisherInput.getText();
        edition = editionInput.getText();
        
        matchFound = false;
        Connection con = null;
        try {
            error.setText("");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
            Statement stmt = con.createStatement();
            
            ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve, Isbn, Copy_number\n" +
                                "FROM BOOK_COPY\n" +
                                "INNER JOIN BOOK ON BOOK_COPY.C_ISBN = BOOK.ISBN\n" +
                                "INNER JOIN BOOK_AUTHORS ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN\n" +
                                "LEFT JOIN ISSUES ON BOOK_COPY.C_isbn = ISSUES.I_Isbn\n" +
                                "AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                "WHERE BOOK_COPY.Is_damaged = 0 AND BOOK.Isbn = '" + isbn + "' OR BOOK.Title = '" + title + "' OR BOOK_AUTHORS.Name = '" + author + "' OR BOOK.Publisher = '" + publisher + "' OR BOOK.Edition = '" + edition + "'\n" +
                                "GROUP BY Isbn, Copy_number\n" +
                                "ORDER BY Return_date DESC");

            while (books.next()) { 
                String theTitle = books.getString("Title");
                String theDate = books.getString("Return_date");
                String checkedOut = books.getString("Is_Checked_Out");
                String onReserve = books.getString("Is_Book_On_Reserve");
                String theIsbn = books.getString("Isbn");
                int copyNumber = books.getInt("Copy_number");
                System.out.println("THE TITLE: " + theTitle);
                addBookToList(theTitle, theDate, checkedOut, onReserve, theIsbn, copyNumber);
                System.out.println("Match found: " + theTitle);
                matchFound = true;
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
    
    private void searchByIsbnEdition() {
        isbn = isbnInput.getText();
        edition = editionInput.getText();
        matchFound = false;
        Connection con = null;
        try {
            error.setText("");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
            Statement stmt = con.createStatement();
            
            ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve, Isbn, Copy_number\n" +
                                "FROM BOOK_COPY\n" +
                                "INNER JOIN BOOK ON BOOK_COPY.C_ISBN = BOOK.ISBN\n" +
                                "INNER JOIN BOOK_AUTHORS ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN\n" +
                                "LEFT JOIN ISSUES ON BOOK_COPY.C_isbn = ISSUES.I_Isbn\n" +
                                "AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                "WHERE BOOK.Isbn = '" + isbn + "' AND BOOK.Edition = '" + edition + "'\n" +
                                "GROUP BY Isbn, Copy_number\n" +
                                "ORDER BY Return_date DESC");

            while (books.next()) { 
                String theTitle = books.getString("Title");
                String theDate = books.getString("Return_date");
                String checkedOut = books.getString("Is_Checked_Out");
                String onReserve = books.getString("Is_Book_On_Reserve");
                String theIsbn = books.getString("Isbn");
                int copyNumber = books.getInt("Copy_number");
                System.out.println("THE TITLE: " + theTitle);
                addBookToList(theTitle, theDate, checkedOut, onReserve, theIsbn, copyNumber);
                System.out.println("Match found: " + theTitle);
                matchFound = true;
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
    
    private void searchByEdition() {
        edition = editionInput.getText();
        matchFound = false;
        Connection con = null;
        try {
            error.setText("");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
            Statement stmt = con.createStatement();
            
            ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve, Isbn, Copy_number\n" +
                                "FROM BOOK_COPY\n" +
                                "INNER JOIN BOOK ON BOOK_COPY.C_ISBN = BOOK.ISBN\n" +
                                "INNER JOIN BOOK_AUTHORS ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN\n" +
                                "LEFT JOIN ISSUES ON BOOK_COPY.C_isbn = ISSUES.I_Isbn\n" +
                                "AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                "WHERE BOOK.Edition = '" + edition + "'\n" +
                                "GROUP BY Isbn, Copy_number\n" +
                                "ORDER BY Return_date DESC");

            while (books.next()) { 
                String theTitle = books.getString("Title");
                String theDate = books.getString("Return_date");
                String checkedOut = books.getString("Is_Checked_Out");
                String onReserve = books.getString("Is_Book_On_Reserve");
                String theIsbn = books.getString("Isbn");
                int copyNumber = books.getInt("Copy_number");
                System.out.println("THE TITLE: " + theTitle);
                addBookToList(theTitle, theDate, checkedOut, onReserve, theIsbn, copyNumber);
                System.out.println("Match found: " + theTitle);
                matchFound = true;
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
    
    private void searchByPublisher() {
        publisher = publisherInput.getText();
        matchFound = false;
        Connection con = null;
        try {
            error.setText("");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
            Statement stmt = con.createStatement();
            
            ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve, Isbn, Copy_number\n" +
                                "FROM BOOK_COPY\n" +
                                "INNER JOIN BOOK ON BOOK_COPY.C_ISBN = BOOK.ISBN\n" +
                                "INNER JOIN BOOK_AUTHORS ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN\n" +
                                "LEFT JOIN ISSUES ON BOOK_COPY.C_isbn = ISSUES.I_Isbn\n" +
                                "AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                "WHERE BOOK.Publisher = '" + publisher + "'\n" +
                                "GROUP BY Isbn, Copy_number\n" +
                                "ORDER BY Return_date DESC");

            while (books.next()) { 
                String theTitle = books.getString("Title");
                String theDate = books.getString("Return_date");
                String checkedOut = books.getString("Is_Checked_Out");
                String onReserve = books.getString("Is_Book_On_Reserve");
                String theIsbn = books.getString("Isbn");
                int copyNumber = books.getInt("Copy_number");
                System.out.println("THE TITLE: " + theTitle);
                addBookToList(theTitle, theDate, checkedOut, onReserve, theIsbn, copyNumber);
                System.out.println("Match found: " + theTitle);
                matchFound = true;
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
    
    private void searchByIsbn() {
        isbn = isbnInput.getText();
        matchFound = false;
        Connection con = null;
        try {
            error.setText("");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
           
            
            Statement stmt = con.createStatement();
            
            ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve, Isbn, Copy_number\n" +
                                "FROM BOOK_COPY\n" +
                                "INNER JOIN BOOK ON BOOK_COPY.C_ISBN = BOOK.ISBN\n" +
                                "INNER JOIN BOOK_AUTHORS ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN\n" +
                                "LEFT JOIN ISSUES ON BOOK_COPY.C_isbn = ISSUES.I_Isbn\n" +
                                "AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                "WHERE BOOK.ISBN = '" + isbn + "'" +
                                "GROUP BY Isbn, Copy_number\n" +
                                "ORDER BY Return_date DESC");

            while (books.next()) { 
                String theTitle = books.getString("Title");
                String theDate = books.getString("Return_date");
                String checkedOut = books.getString("Is_Checked_Out");
                String onReserve = books.getString("Is_Book_On_Reserve");
                String theIsbn = books.getString("Isbn");
                int copyNumber = books.getInt("Copy_number");
                System.out.println("THE TITLE: " + theTitle);
                addBookToList(theTitle, theDate, checkedOut, onReserve, theIsbn, copyNumber);
                System.out.println("Match found: " + theTitle);
                matchFound = true;
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
            error.setText("");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
           
            
            Statement stmt = con.createStatement();

            // TODO: if not available: show the date when the book will be available, which is based on last check-out OR hold-status
            ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve, Isbn, Copy_number\n" +
                                "FROM BOOK_COPY\n" +
                                "INNER JOIN BOOK ON BOOK_COPY.C_ISBN = BOOK.ISBN\n" +
                                "INNER JOIN BOOK_AUTHORS ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN\n" +
                                "LEFT JOIN ISSUES ON BOOK_COPY.C_isbn = ISSUES.I_Isbn\n" +
                                "AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                "WHERE BOOK.Title = '" + title + "'" +
                                "GROUP BY Isbn, Copy_number\n" +
                                "ORDER BY Return_date DESC");

            while (books.next()) {
                String theTitle = books.getString("Title");
                String theDate = books.getString("Return_date");
                String checkedOut = books.getString("Is_Checked_Out");
                String onReserve = books.getString("Is_Book_On_Reserve");
                String theIsbn = books.getString("Isbn");
                int copyNumber = books.getInt("Copy_number");
                addBookToList(theTitle, theDate, checkedOut, onReserve, theIsbn, copyNumber);
                System.out.println("Match found: " + theTitle);
                matchFound = true;
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
    
    private void searchByAuthor() {
        author = authorInput.getText();
        matchFound = false;
        Connection con = null;
        try {
            error.setText("");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");
            if(!con.isClosed())
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            
           
            
        Statement stmt = con.createStatement();
        
        // TODO: if not available: show the date when the book will be available, which is based on last check-out OR hold-status
        ResultSet books = stmt.executeQuery("SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve, Isbn, Copy_number\n" +
                                "FROM BOOK_COPY\n" +
                                "INNER JOIN BOOK ON BOOK_COPY.C_ISBN = BOOK.ISBN\n" +
                                "INNER JOIN BOOK_AUTHORS ON BOOK.Isbn = BOOK_AUTHORS.B_ISBN\n" +
                                "LEFT JOIN ISSUES ON BOOK_COPY.C_isbn = ISSUES.I_Isbn\n" +
                                "AND BOOK_COPY.Copy_number = ISSUES.I_copy_no\n" +
                                "WHERE BOOK_AUTHORS.Name = '" + author + "'" +
                                "GROUP BY Isbn, Copy_number\n" +
                                "ORDER BY Return_date DESC");
                                          
        while (books.next()) {
            String theTitle = books.getString("Title");
            String theDate = books.getString("Return_date");
            String checkedOut = books.getString("Is_Checked_Out");
            String onReserve = books.getString("Is_Book_On_Reserve");
            String theIsbn = books.getString("Isbn");
            int copyNumber = books.getInt("Copy_number");
            addBookToList(theTitle, theDate, checkedOut, onReserve, theIsbn, copyNumber);
            System.out.println("Match found: " + theTitle);
            matchFound = true;
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
    
    private void goToShowBooksScreen() {
        try {
                Parent foster = LMS.getParent();
                Stage stage = LMS.getStage();
                foster = FXMLLoader.load(getClass().getResource("ShowBooks.fxml"));
        
                Scene scene = new Scene(foster);
        
                stage.setScene(scene);
                stage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    private void addBookToList(String title, String date, String checkedOut, String onReserve, String theIsbn, int copyNumber) {
        Boolean isCheckedOut = false;
        Boolean isOnReserve = false;
        isCheckedOut = checkedOut.equalsIgnoreCase("true") || checkedOut.equalsIgnoreCase("1");
        isOnReserve = onReserve.equalsIgnoreCase("true") || onReserve.equalsIgnoreCase("1");
        Book b = new Book(title, isCheckedOut, date, isOnReserve, theIsbn, copyNumber);
        booksFound.add(b);
    }
    
    public static HashSet<Book> getBooksFound() {
        return booksFound;
    }
}

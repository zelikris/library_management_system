/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kris
 */
public class ShowBooksController implements Initializable {
    
    @FXML
    private TableView<Book> bigTable;
    @FXML
    private TableColumn<Book, Boolean> isCheckedOut;
    @FXML
    private TableColumn<Book, Boolean> isOnReserve;
    @FXML
    private TableColumn<Book, String> returnDate;
    @FXML
    private TableColumn<Book, String> title;
    @FXML
    private TableColumn<Book, String> isbn;
    @FXML
    private TableColumn<Book, Integer> copyNumber;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        HashSet<Book> booksFound = SearchBookController.getBooksFound();
                
        ObservableList<Book> list = FXCollections.observableArrayList(booksFound);
        
        title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        returnDate.setCellValueFactory(new PropertyValueFactory<Book, String>("returnDate"));
        isCheckedOut.setCellValueFactory(new PropertyValueFactory<Book, Boolean>("isCheckedOut"));
        isOnReserve.setCellValueFactory(new PropertyValueFactory<Book, Boolean>("isOnReserve"));
        isbn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        copyNumber.setCellValueFactory(new PropertyValueFactory<Book, Integer>("copyNumber"));
        
        bigTable.setItems(list);       
    }    
    
    @FXML
    private void onBackEvent(MouseEvent event) {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("SearchBook.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}

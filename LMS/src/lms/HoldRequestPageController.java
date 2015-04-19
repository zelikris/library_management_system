/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private ComboBox selectAvail;
    
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
    }    
    
    @FXML
    private void onBackEvent(MouseEvent event)  {
        try {
            LMS.setSessionUser(null);
            LMS.setRegistrationPassword(null);
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

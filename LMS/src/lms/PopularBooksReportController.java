/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author keatts
 */
public class PopularBooksReportController implements Initializable {

    @FXML Button backButton;
    @FXML Button updateButton;
    @FXML TableColumn monthCol;
    @FXML TableColumn titleCol;
    @FXML TableColumn checkoutsCol;
    @FXML TableView table;
    
    private final ObservableList<PopularBook> data =
        FXCollections.observableArrayList(
            new PopularBook("Jan", "Fundamentals of Databases", 14),
            new PopularBook("Jan", "Data Mining Principles", 8),
            new PopularBook("Jan", "Internetworking with TCP/IP", 10),
            new PopularBook("Feb", "Data Mining Principles", 22),
            new PopularBook("Feb", "Object Oriented Software Engineering", 21),
            new PopularBook("Feb", "Fundamentals of Databases", 5)
        );

    public static class PopularBook {
        private final SimpleStringProperty month;
        private final SimpleStringProperty title;
        private final SimpleIntegerProperty checkouts;
        
        private PopularBook(String month, String title, int checkouts) {
            this.month = new SimpleStringProperty(month);
            this.title = new SimpleStringProperty(title);
            this.checkouts = new SimpleIntegerProperty(checkouts);
        }
        
        public String getMonth() {
            return month.get();
        }
        
        public void setMonth(String month) {
            this.month.set(month);
        }
        
        public String getTitle() {
            return title.get();
        }
        
        public void setTitle(String title) {
            this.title.set(title);
        }
        
        public int getCheckouts() {
            return checkouts.get();
        }
        
        public void setCheckouts(int checkouts) {
            this.checkouts.set(checkouts);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void backAction() {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("ReportsPage.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTable() {
        monthCol.setCellValueFactory(
                new PropertyValueFactory <>("month"));
        titleCol.setCellValueFactory(
                new PropertyValueFactory <>("title"));
        checkoutsCol.setCellValueFactory(
                new PropertyValueFactory <>("checkouts"));
        table.setItems(data);
    }

}

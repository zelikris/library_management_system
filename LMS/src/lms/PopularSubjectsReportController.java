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
public class PopularSubjectsReportController implements Initializable {

    @FXML Button backButton;
    @FXML Button updateButton;
    @FXML TableColumn monthCol;
    @FXML TableColumn subjectCol;
    @FXML TableColumn checkoutsCol;
    @FXML TableView table;

    private final ObservableList<PopularSubject> data =
        FXCollections.observableArrayList(
            new PopularSubject("Jan", "Computer Science", 98),
            new PopularSubject("Jan", "History", 54),
            new PopularSubject("Jan", "Mathematics", 52),
            new PopularSubject("Feb", "Computer Science", 121),
            new PopularSubject("Feb", "Psychology", 32),
            new PopularSubject("Feb", "Mathematics", 21)
        );

    public static class PopularSubject {
        private final SimpleStringProperty month;
        private final SimpleStringProperty subject;
        private final SimpleIntegerProperty checkouts;
        
        private PopularSubject(String month, String subject, int checkouts) {
            this.month = new SimpleStringProperty(month);
            this.subject = new SimpleStringProperty(subject);
            this.checkouts = new SimpleIntegerProperty(checkouts);
        }
        
        public String getMonth() {
            return month.get();
        }
        
        public void setMonth(String month) {
            this.month.set(month);
        }
        
        public String getSubject() {
            return subject.get();
        }
        
        public void setSubject(String subject) {
            this.subject.set(subject);
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
        subjectCol.setCellValueFactory(
                new PropertyValueFactory <>("subject"));
        checkoutsCol.setCellValueFactory(
                new PropertyValueFactory <>("checkouts"));
        table.setItems(data);
    }

}

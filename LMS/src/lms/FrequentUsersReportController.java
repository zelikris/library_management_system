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
public class FrequentUsersReportController implements Initializable {

    @FXML Button backButton;
    @FXML Button updateButton;
    @FXML TableColumn monthCol;
    @FXML TableColumn usernameCol;
    @FXML TableColumn checkoutsCol;
    @FXML TableView table;

    private final ObservableList<FrequentUser> data =
        FXCollections.observableArrayList(
            new FrequentUser("Jan", "Malvika Paul", 18),
            new FrequentUser("Jan", "Gayatri Singh", 14),
            new FrequentUser("Feb", "Anthony Tsou", 22),
            new FrequentUser("Feb", "Gayatri Singh", 21),
            new FrequentUser("Feb", "Chong Guo", 12),
            new FrequentUser("Feb", "Sen Lin", 11),
            new FrequentUser("Feb", "Amol Parikh", 11)
        );

    public static class FrequentUser {
        private final SimpleStringProperty month;
        private final SimpleStringProperty username;
        private final SimpleIntegerProperty checkouts;
        
        private FrequentUser(String month, String username, int checkouts) {
            this.month = new SimpleStringProperty(month);
            this.username = new SimpleStringProperty(username);
            this.checkouts = new SimpleIntegerProperty(checkouts);
        }
        
        public String getMonth() {
            return month.get();
        }
        
        public void setMonth(String month) {
            this.month.set(month);
        }
        
        public String getUsername() {
            return username.get();
        }
        
        public void setTitle(String username) {
            this.username.set(username);
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
        usernameCol.setCellValueFactory(
                new PropertyValueFactory <>("username"));
        checkoutsCol.setCellValueFactory(
                new PropertyValueFactory <>("checkouts"));
        table.setItems(data);
    }

}

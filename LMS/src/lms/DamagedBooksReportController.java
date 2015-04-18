/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author keatts
 */
public class DamagedBooksReportController implements Initializable {

    @FXML Button showReportButton, backButton;
    @FXML ComboBox monthBox, subjectBoxOne, subjectBoxTwo, subjectBoxThree;
    @FXML TableColumn monthCol, subjectCol, numDamagedCol;
    @FXML TableView table;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthBox.getItems().addAll("January", "February", "March", "April",
                "May", "June", "July", "August", "September",
                "October", "November", "December");
    }
    
}

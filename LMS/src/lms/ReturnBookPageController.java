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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author keatts
 */
public class ReturnBookPageController implements Initializable {

    @FXML ComboBox conditionBox;
    @FXML TextField issueIdField, isbnField, copyNoField, usernameField;
    @FXML TableColumn selectCol, isbnCol, bookCol, editionCol, copiesCol;
    @FXML TableView table;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conditionBox.getItems().addAll("N", "Y");
        conditionBox.setValue("N");
    }    
    
}

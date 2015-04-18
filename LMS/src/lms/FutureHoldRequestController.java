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
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author zain
 */
public class FutureHoldRequestController implements Initializable {

    @FXML
    private Button requestButton;
    @FXML
    private Button okayButton;
    @FXML
    private TextField isbn;
    @FXML
    private TextField copyNumber;
    @FXML
    private TextField availableDate;
    @FXML
    private Button backButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

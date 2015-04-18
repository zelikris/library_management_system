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
public class RequestExtensionPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button submitButton;
    @FXML
    private TextField issueID;
    @FXML
    private TextField currentExtensionDate;
    @FXML
    private TextField currentReturnDate;
    @FXML
    private TextField originalCheckoutDate;
    @FXML
    private TextField newExtensionDate;
    @FXML
    private TextField newReturnDate;
    @FXML
    private Button submitExtensionRequest;
    @FXML
    private Button backButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

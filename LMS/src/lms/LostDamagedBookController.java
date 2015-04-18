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
public class LostDamagedBookController implements Initializable {

    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button searchLastUser;
    @FXML
    private TextField isbn;
    @FXML
    private TextField bookCopyNumber;
    @FXML
    private TextField currentTime;
    @FXML
    private TextField lastUserOfBook;
    @FXML
    private TextField amountToCharge;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

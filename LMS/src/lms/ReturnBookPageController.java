/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author keatts
 */
public class ReturnBookPageController implements Initializable {

    @FXML Button backButton;
    @FXML Button returnButton;
    @FXML ComboBox conditionBox;
    @FXML TextField issueIdField;
    @FXML TextField isbnField;
    @FXML TextField copyNoField;
    @FXML TextField usernameField;

    public void backAction() {
        try {
            Parent foster = LMS.getParent();
            Stage stage = LMS.getStage();
            foster = FXMLLoader.load(getClass().getResource("Home.fxml"));

            Scene scene = new Scene(foster);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conditionBox.getItems().addAll("N", "Y");
        conditionBox.setValue("N");
    }
    
}

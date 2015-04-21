/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kris
 */
public class CreateProfilePageController implements Initializable {

    @FXML
    private TextField firstName;   
    @FXML
    private DatePicker dateOfBirth;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    @FXML
    private Button cancelButton;    
    @FXML
    private TextField lastName;    
    @FXML
    private ComboBox<String> gender;
    @FXML
    private CheckBox isFaculty;
    @FXML
    private TextField department;
    @FXML
    private Button registerButton;
    @FXML
    private Text departmentLabel;
    @FXML
    private Text error;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gender.getItems().addAll("Male", "Female");
        
        isFaculty.selectedProperty().addListener(new ChangeListener<Boolean>() {
        public void changed(ObservableValue<? extends Boolean> ov,
            Boolean old_val, Boolean new_val) {
                department.setVisible(new_val);
                departmentLabel.setVisible(new_val);
        }
    });
    }
    
    @FXML
    private void onRegisterEvent(MouseEvent event) {
        error.setText("");
        if (checkNulls()) {
            insertToDB();
            goToHomePage();
        }
    }
    
    private void goToHomePage() {
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
    
    private void insertToDB() {
        String fName = firstName.getText();
        String lName = lastName.getText();
        String fullName = fName + " " + lName;
        LocalDate dob = dateOfBirth.getValue();
        //String dobSqlDate = convertToSQLDate(dob);
        
        String addrs = address.getText();
        String gendr = gender.getValue();
        boolean isAFaculty = isFaculty.isSelected();
        String dept = "NULL";
        if (isAFaculty) {
            dept = department.getText();
        }
        String theEmail = email.getText();
        Connection con = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
                "ayt2V3Ck");
            Statement stmt = con.createStatement();

            stmt.executeUpdate("INSERT INTO USER VALUES('"+ LMS.getSessionUser() +"','" + LMS.getRegistrationPassword() + "')");
            stmt.executeUpdate("INSERT INTO STUDENT_FACULTY(Sf_username, Name, "
                    + "Dob, Gender, "
                    + "Email, Address, Is_faculty, "
                    + "Dept)\n"
                    + "VALUES ('" + LMS.getSessionUser() + "', "
                    + "'" + fullName + "', '" + dob + "', '" + gendr + "', '"+ theEmail +"', "
                    + "'" + addrs + "', " + isAFaculty + ", '" + dept + "')");
            //Export to function to stop the while loop with a return

        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            try {
                if(con != null)
                con.close();
            } catch(SQLException e) {
                System.err.println(e);
            }   
        }    
    }
    
    @FXML
    private void cancelEvent(MouseEvent event) {
        LMS.setSessionUser(null);
        LMS.setRegistrationPassword(null);
        try {
                Parent foster = LMS.getParent();
                Stage stage = LMS.getStage();
                foster = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        
                Scene scene = new Scene(foster);
        
                stage.setScene(scene);
                stage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    private String convertToSQLDate(LocalDate date) {
        int month = date.getMonthValue();
        int year = date.getYear();
        int day = date.getDayOfMonth();
        return month + "/" + day + "/" + year;
    }
    
    private boolean checkNulls() {
            if (firstName.getText().equals("")) {
                error.setText("First name must be filled out");
                return false;
            }
            //Gender;
            else if (dateOfBirth.getValue() == null) {
                error.setText("DOB must be filled out");
                return false;
            }
            //if (Integer.parseInt(salary.getText()) < 1) {
            else if (address.getText().equals("")) {  
                error.setText("Address must be filled out");
                return false;
            }
            else if (lastName.getText().equals("")) {
                error.setText("Last name must be filled out");
                return false;
            }
            else if (isFaculty.isSelected()) {
                if (department.getText().equals("")) {
                    error.setText("Dept must be filled out");
                    return false;
                }
            }
            else if (gender.getValue() == null) {
                error.setText("Gender must be filled out");
                return false;
            }
            else if (email.getText().equals("")) {
                error.setText("Email must be filled out");
                return false;
            }
            System.out.println("Null check success");
            return true;
    }
    
}
    
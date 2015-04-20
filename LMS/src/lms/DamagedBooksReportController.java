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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author keatts
 */
public class DamagedBooksReportController implements Initializable {

    @FXML Button showReportButton;
    @FXML Button backButton;
    @FXML ComboBox monthBox;
    @FXML ComboBox subjectBoxOne;
    @FXML ComboBox subjectBoxTwo;
    @FXML ComboBox subjectBoxThree;
    @FXML TableColumn monthCol;
    @FXML TableColumn subjectCol;
    @FXML TableColumn numDamagedCol;
    @FXML TableView table;

    private final ObservableList<DamagedBooksSubject> data = FXCollections.observableArrayList();

    public static class DamagedBooksSubject {
        private final SimpleStringProperty month;
        private final SimpleStringProperty subject;
        private final SimpleIntegerProperty count;

        private DamagedBooksSubject(String month, String subject, int count) {
            this.month = new SimpleStringProperty(month);
            this.subject = new SimpleStringProperty(subject);
            this.count = new SimpleIntegerProperty(count);
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

        public int getCount() {
            return count.get();
        }

        public void setCount(int count) {
            this.count.set(count);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthBox.getItems().addAll("January", "February", "March", "April",
                "May", "June", "July", "August", "September",
                "October", "November", "December");
        
        populateSubjects();
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

    public void populateSubjects() {
        Connection con = null;
        ArrayList<String> subjs = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");

            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT S_name FROM SUBJECT");

            while (results.next()) {
                String subject = results.getString("S_name");

                subjs.add(subject);
            }
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch(SQLException e) {
                System.err.println(e);
            }
        }

        subjectBoxOne.getItems().addAll(subjs);
        subjectBoxTwo.getItems().addAll(subjs);
        subjectBoxThree.getItems().addAll(subjs);
    }

    public void showReportAction() {
        Connection con = null;
        
        String subjectOne = subjectBoxOne.getValue().toString();
        String subjectTwo = subjectBoxTwo.getValue().toString();
        String subjectThree = subjectBoxThree.getValue().toString();
        int month = 0;

        switch (monthBox.getValue().toString()) {
            case "January":
                month = 1;
                break;
            case "February":
                month = 2;
                break;
            case "March":
                month = 3;
                break;
            case "April":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "June":
                month = 6;
                break;
            case "July":
                month = 7;
                break;
            case "August":
                month = 8;
                break;
            case "September":
                month = 9;
                break;
            case "October":
                month = 10;
                break;
            case "November":
                month = 11;
                break;
            case "December":
                month = 12;
                break;
        }
        
        if (month == 0) {
            System.out.println("You must select a month");
        }

        if ("".equals(subjectOne) || "".equals(subjectTwo) || "".equals(subjectThree)) {
            System.out.println("You must select three subjects (they can be the same)");
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");

            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT MONTH(Date_of_issue), S_name, COUNT(*) FROM (\n" +
                                                  "    SELECT BOOK.Isbn, BOOK_COPY.Copy_number, ISSUES.Date_of_issue, SUBJECT.S_name\n" +
                                                  "    FROM BOOK\n" +
                                                  "    INNER JOIN BOOK_COPY\n" +
                                                  "    ON BOOK.Isbn = BOOK_COPY.C_isbn\n" +
                                                  "    INNER JOIN SUBJECT\n" +
                                                  "    ON BOOK.Subject_name = SUBJECT.S_name\n" +
                                                  "    INNER JOIN ISSUES\n" +
                                                  "    ON BOOK.Isbn = ISSUES.I_isbn\n" +
                                                  "    WHERE Is_damaged = 1 AND MONTH(ISSUES.Date_of_issue) = " + Integer.toString(month) + " AND (S_name = '" + subjectOne + "' OR S_name = '" + subjectTwo + "' OR S_name = '" + subjectThree + "')\n" +
                                                  "    GROUP BY Isbn, Copy_number\n" +
                                                  "    ORDER BY S_name, Isbn, Copy_number ASC\n" +
                                                  ") T2\n" +
                                                  "GROUP BY MONTH(Date_of_issue), S_name\n");

            while (results.next()) {
                String subject = results.getString("S_name");
                Integer count = results.getInt("COUNT(*)");

                data.add(new DamagedBooksSubject(monthBox.getValue().toString(), subject, count));
            }
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch(SQLException e) {
                System.err.println(e);
            }
        }
        
        monthCol.setCellValueFactory(
                new PropertyValueFactory <>("month"));
        subjectCol.setCellValueFactory(
                new PropertyValueFactory <>("subject"));
        numDamagedCol.setCellValueFactory(
                new PropertyValueFactory <>("count"));
        table.setItems(data);
    }

}

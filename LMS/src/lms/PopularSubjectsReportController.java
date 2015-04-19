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

    private final ObservableList<PopularSubject> data = FXCollections.observableArrayList();

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
        Connection con = null;

        String[] months;
        months = new String[12];
        months[0] = "Jan";
        months[1] = "Feb";
        months[2] = "Mar";
        months[3] = "Apr";
        months[4] = "May";
        months[5] = "Jun";
        months[6] = "Jul";
        months[7] = "Aug";
        months[8] = "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_22", "cs4400_Group_22",
            "ayt2V3Ck");

            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM\n" +
                                                "(\n" +
                                                "    SELECT SUBJECT.S_Name, MONTH(ISSUES.Date_of_issue), COUNT(BOOK.Title)\n" +
                                                "    FROM BOOK\n" +
                                                "    INNER JOIN SUBJECT\n" +
                                                "    ON BOOK.Subject_Name = SUBJECT.S_Name\n" +
                                                "    INNER JOIN ISSUES\n" +
                                                "    ON BOOK.ISBN = ISSUES.I_ISBN\n" +
                                                "    WHERE MONTH(ISSUES.Date_of_issue) = 3\n" +
                                                "    GROUP BY SUBJECT.S_Name, MONTH(ISSUES.Date_of_issue)\n" +
                                                "    ORDER BY COUNT(SUBJECT.S_Name) DESC\n" +
                                                ") T1\n" +
                                                "UNION\n" +
                                                "SELECT * FROM\n" +
                                                "(\n" +
                                                "    SELECT SUBJECT.S_Name, MONTH(ISSUES.Date_of_issue), COUNT(BOOK.Title)\n" +
                                                "    FROM BOOK\n" +
                                                "    INNER JOIN SUBJECT\n" +
                                                "    ON BOOK.Subject_Name = SUBJECT.S_Name\n" +
                                                "    INNER JOIN ISSUES\n" +
                                                "    ON BOOK.ISBN = ISSUES.I_ISBN\n" +
                                                "    WHERE MONTH(ISSUES.Date_of_issue) = 4\n" +
                                                "    GROUP BY SUBJECT.S_Name, MONTH(ISSUES.Date_of_issue)\n" +
                                                "    ORDER BY COUNT(SUBJECT.S_Name) DESC\n" +
                                                ") T2");

            while (results.next()) {
                String subject = results.getString("S_Name");
                String month  = months[Integer.parseInt(results.getString("MONTH(ISSUES.Date_of_issue)"))];
                Integer checkouts = Integer.parseInt(results.getString("COUNT(BOOK.Title)"));

                data.add(new PopularSubject(month, subject, checkouts));
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
        checkoutsCol.setCellValueFactory(
                new PropertyValueFactory <>("checkouts"));
        table.setItems(data);
    }

}

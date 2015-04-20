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
public class FrequentUsersReportController implements Initializable {

    @FXML Button backButton;
    @FXML TableColumn monthCol;
    @FXML TableColumn usernameCol;
    @FXML TableColumn checkoutsCol;
    @FXML TableView table;

    private final ObservableList<FrequentUser> data = FXCollections.observableArrayList();

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
        updateTable();
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
                                                "    SELECT USER.Username, COUNT(USER.Username), MONTH(ISSUES.Date_of_issue)\n" +
                                                "    FROM USER\n" +
                                                "    INNER JOIN ISSUES\n" +
                                                "    ON USER.Username = ISSUES.I_sf_username\n" +
                                                "    WHERE MONTH(ISSUES.Date_of_issue) = 3\n" +
                                                "    GROUP BY USER.Username, MONTH(ISSUES.Date_of_issue)\n" +
                                                "    HAVING COUNT(USER.Username) > 10\n" +
                                                "    ORDER BY COUNT(USER.Username) DESC\n" +
                                                "    LIMIT 5\n" +
                                                ") T1\n" +
                                                "UNION\n" +
                                                "SELECT * FROM\n" +
                                                "(\n" +
                                                "    SELECT USER.Username, COUNT(USER.Username), MONTH(ISSUES.Date_of_issue)\n" +
                                                "    FROM USER\n" +
                                                "    INNER JOIN ISSUES\n" +
                                                "    ON USER.Username = ISSUES.I_sf_username\n" +
                                                "    WHERE MONTH(ISSUES.Date_of_issue) = 4\n" +
                                                "    GROUP BY USER.Username, MONTH(ISSUES.Date_of_issue)\n" +
                                                "    HAVING COUNT(USER.Username) > 10\n" +
                                                "    ORDER BY COUNT(USER.Username) DESC\n" +
                                                "    LIMIT 5\n" +
                                                ") T2");

            while (results.next()) {
                String username = results.getString("Username");
                Integer checkouts = Integer.parseInt(results.getString("COUNT(USER.Username)"));
                String month  = months[Integer.parseInt(results.getString("MONTH(ISSUES.Date_of_issue)"))];

                data.add(new FrequentUser(month, username, checkouts));
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
        usernameCol.setCellValueFactory(
                new PropertyValueFactory <>("username"));
        checkoutsCol.setCellValueFactory(
                new PropertyValueFactory <>("checkouts"));
        table.setItems(data);
    }

}

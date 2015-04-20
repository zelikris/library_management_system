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
public class PopularBooksReportController implements Initializable {

    @FXML Button backButton;
    @FXML TableColumn monthCol;
    @FXML TableColumn titleCol;
    @FXML TableColumn checkoutsCol;
    @FXML TableView table;

    private final ObservableList<PopularBook> data = FXCollections.observableArrayList();

    public static class PopularBook {
        private final SimpleStringProperty month;
        private final SimpleStringProperty title;
        private final SimpleIntegerProperty checkouts;

        private PopularBook(String month, String title, int checkouts) {
            this.month = new SimpleStringProperty(month);
            this.title = new SimpleStringProperty(title);
            this.checkouts = new SimpleIntegerProperty(checkouts);
        }

        public String getMonth() {
            return month.get();
        }

        public void setMonth(String month) {
            this.month.set(month);
        }
        
        public String getTitle() {
            return title.get();
        }

        public void setTitle(String title) {
            this.title.set(title);
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
                                                "    SELECT BOOK.Title, COUNT(BOOK.Title), MONTH(ISSUES.Date_of_issue)\n" +
                                                "    FROM BOOK\n" +
                                                "    INNER JOIN ISSUES\n" +
                                                "    ON BOOK.Isbn = ISSUES.I_isbn\n" +
                                                "    WHERE MONTH(ISSUES.Date_of_issue) = 3\n" +
                                                "    GROUP BY BOOK.Isbn, MONTH(ISSUES.Date_of_issue)\n" +
                                                "    ORDER BY COUNT(BOOK.Title) DESC\n" +
                                                "    LIMIT 3\n" +
                                                ") T1\n" +
                                                "UNION\n" +
                                                "SELECT * FROM\n" +
                                                "(\n" +
                                                "    SELECT BOOK.Title, COUNT(BOOK.Title), MONTH(ISSUES.Date_of_issue)\n" +
                                                "    FROM BOOK\n" +
                                                "    INNER JOIN ISSUES\n" +
                                                "    ON BOOK.Isbn = ISSUES.I_isbn\n" +
                                                "    WHERE MONTH(ISSUES.Date_of_issue) = 4\n" +
                                                "    GROUP BY BOOK.Isbn, MONTH(ISSUES.Date_of_issue)\n" +
                                                "    ORDER BY COUNT(BOOK.Title) DESC\n" +
                                                "    LIMIT 3\n" +
                                                ") T2");

            while (results.next()) {
                String title = results.getString("Title");
                Integer checkouts = Integer.parseInt(results.getString("COUNT(BOOK.Title)"));
                String month  = months[Integer.parseInt(results.getString("MONTH(ISSUES.Date_of_issue)"))];

                data.add(new PopularBook(month, title, checkouts));
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
        titleCol.setCellValueFactory(
                new PropertyValueFactory <>("title"));
        checkoutsCol.setCellValueFactory(
                new PropertyValueFactory <>("checkouts"));
        table.setItems(data);
    }

}

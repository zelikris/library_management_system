/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.RadioButton;

/**
 *
 * @author Kris
 */
public class Book {
    private String isbn;
    private String title;
    private String edition;
    private Integer copies;
    private Boolean isCheckedOut;
    private String returnDate;
    private Boolean isOnReserve;
    private SimpleObjectProperty<RadioButton> selected;
    private int copiesAvailable;
    private int copyNumber;
    
    // for search books
    public Book(String aTitle, Boolean checkedOut, String aReturnDate, Boolean onReserve, String theIsbn, int cNumber) {
        title = aTitle;
        isCheckedOut = checkedOut;
        returnDate = aReturnDate;
        isOnReserve = onReserve;
        isbn = theIsbn;
        copyNumber = cNumber;
    }

//    public Book(Boolean select, String aIsbn, String aTitle, String aEdition, Integer aCopies) {
//        selected = select;
//        isbn = aIsbn;
//        title = aTitle;
//        edition = aEdition;
//        copies = aCopies;
//    }
    
    // for hold request
    public Book(String theIsbn, String theTitle, String theEdition, Boolean onReserve, int available, int aCopyNumber) {
        isbn = theIsbn;
        title = theTitle;
        edition = theEdition;
        onReserve = isOnReserve;
        copiesAvailable = available;
        copyNumber = aCopyNumber;
        
    }
    
    public int getCopyNumber() {
        return copyNumber;
    }
    
    public int getCopiesAvailable() {
        return copiesAvailable;
    }
    
    public Boolean getIsCheckedOut() {
        return isCheckedOut;
    }
    
    public String getReturnDate() {
        return returnDate;
    }
    
    public Boolean getIsOnReserve() {
        return isOnReserve;
    }

    public SimpleObjectProperty<RadioButton> getSelected() {
        return selected;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getEdition() {
        return edition;
    }

    public Integer getCopies() {
        return copies;
    }
    
    @Override
    public String toString() {
        return getTitle();
    }
}

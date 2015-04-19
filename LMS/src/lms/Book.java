/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

/**
 *
 * @author Kris
 */
public class Book {
    private Boolean selected;
    private String isbn;
    private String title;
    private Integer edition;
    private Integer copies;
    private Boolean isCheckedOut;
    private String returnDate;
    private Boolean isOnReserve;
    
    //SELECT BOOK.Title, BOOK_COPY.Is_Checked_Out, ISSUES.Return_date, BOOK.Is_Book_On_Reserve\n
    
    public Book(String aTitle, Boolean checkedOut, String aReturnDate, Boolean onReserve) {
        title = aTitle;
        isCheckedOut = checkedOut;
        returnDate = aReturnDate;
        isOnReserve = onReserve;
    }

    public Book(Boolean select, String aIsbn, String aTitle, Integer aEdition, Integer aCopies) {
        selected = select;
        isbn = aIsbn;
        title = aTitle;
        edition = aEdition;
        copies = aCopies;
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

    public Boolean getSelected() {
        return selected;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Integer getEdition() {
        return edition;
    }

    public Integer getCopies() {
        return copies;
    }
}

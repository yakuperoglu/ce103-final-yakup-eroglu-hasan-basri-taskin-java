/**
 * The {@code librarysystem} package contains classes related to the library system application.
 * <p>
 * This package includes entities such as {@link User} and {@link Book}, representing users and books
 * in the library system, respectively.
 * </p>
 * <p>
 * Classes in this package are designed to provide functionality for managing library resources,
 * user accounts, and other aspects of a library system.
 * </p>
 * 
 * @author Hasan Yakup
 * @version 1.0
 * @since 2024-01-11
 */
package com.hasan.yakup.librarysystem;

/**
 * The {@code Book} class represents a book in the library system with
 * attributes such as ID, name, marked status, wishlist status, and loaned status.
 * <p>
 * This class provides methods to access and modify the book's information.
 * </p>
 * 
 * @author Hasan Yakup
 * @version 1.0
 * @since 2024-01-11
 */
public class Book {
     /**
     * The unique identifier of the book.
     */
    private int id;
    /**
     * The name/title of the book.
     */
    private String name;
    /**
     * Indicates whether the book is marked by the user.
     */
    private boolean isMarked;
      /**
     * Indicates whether the book is in the user's wishlist.
     */
    private boolean isWishlist;
    /**
     * Indicates whether the book is currently loaned.
     */
    private boolean isLoaned;

    /**
     * Default constructor for the {@code Book} class.
     */
    public Book() {
    }

     /**
     * Parameterized constructor for the {@code Book} class.
     *
     * @param bookId     The unique identifier of the book.
     * @param bookName   The name/title of the book.
     * @param marked     Indicates whether the book is marked by the user.
     * @param wishlist   Indicates whether the book is in the user's wishlist.
     * @param loaned     Indicates whether the book is currently loaned.
     */
    public Book(int bookId, String bookname, boolean isMarked, boolean isWishlist, boolean isLoaned) {
        this.id = bookId;
        this.name = bookname;
        this.isMarked = isMarked;
        this.isWishlist = isWishlist;
        this.isLoaned = isLoaned;
    }

    /**
     * Gets the unique identifier of the book.
     *
     * @return The unique identifier of the book.
     */
    public int getId() {
        return id;
    }

     /**
     * Sets the unique identifier of the book.
     *
     * @param id The new unique identifier to be set.
     */
    public void setId(int id) {
        this.id = id;
    }

     /**
     * Gets the name/title of the book.
     *
     * @return The name/title of the book.
     */
    public String getName() {
        return name;
    }

    /**
 * The {@code Book} class represents a book in the library system with
 * attributes such as ID, name, marked status, wishlist status, and loaned status.
 * <p>
 * This class provides methods to access and modify the book's information.
 * </p>
 * 
 * @author Hasan Yakup
 * @version 1.0
 * @since 2024-01-11
 */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Indicates whether the book is marked by the user.
     */
    public boolean isMarked() {
        return isMarked;
    }

     /**
     * The name/title of the book.
     */
    public void setMarked(boolean marked) {
        isMarked = marked;
    }

     /**
     * Indicates whether the book is in the user's wishlist.
     */
    public boolean isWishlist() {
        return isWishlist;
    }

    /**
    * Sets the wishlist status of the book.
    *
    * @param wishlist The new wishlist status to be set.
    */
    public void setWishlist(boolean wishlist) {
        isWishlist = wishlist;
    }

    /**
     * Checks if the book is currently loaned.
     *
     * @return {@code true} if the book is loaned, {@code false} otherwise.
     */
    public boolean isLoaned() {
        return isLoaned;
    }

    /**
     * Sets the loaned status of the book.
     *
     * @param loaned The new loaned status to be set.
     */
    public void setLoaned(boolean loaned) {
        isLoaned = loaned;
    }
}

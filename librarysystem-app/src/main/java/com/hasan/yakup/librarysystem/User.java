/**
 * The {@code librarysystem} package contains classes related to the library system application.
 * <p>
 * This package includes entities such as {@link User} representing users in the library system.
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
 * The {@code User} class represents a user in the library system with
 * attributes such as email and password.
 * <p>
 * This class provides methods to access and modify the user's email and password.
 * </p>
 * 
 * @author Hasan Yakup
 * @version 1.0
 * @since 2024-01-11
 */
public class User {

    /**
     * The email address of the user.
     */
	private String email;

     /**
     * The password of the user.
     */
    private String password;


    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */

    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The new email address to be set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password to be set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

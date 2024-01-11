/**
 * @file Librarysystem.java
 * @brief A simple library system implementation with basic functionalities.
 * @author Hasan Yakup
 * @date [Date]
 */
package com.hasan.yakup.librarysystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @class Librarysystem
 * @brief Represents a library system with basic operations on books.
 */
public class Librarysystem {
    private Scanner scanner;
    private PrintStream out;

    /**
     * @brief Constructor for Librarysystem.
     * @param scanner Scanner object for user input.
     * @param out     PrintStream object for output.
     */
    public Librarysystem(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    /**
     * @brief Gets a new unique ID for a book based on the existing books in the
     *        library.
     * @param pathFileBooks The path to the file containing book information.
     * @return A new unique ID for a book.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public int getNewId(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        return books.size() + 1;
    }

    /**
     * @brief Clears the console screen.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    public void clearScreen() throws InterruptedException, IOException {
        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            out.print("\033[H\033[2J");
            out.flush();
        }
    }

    /**
     * @brief Handles input errors by displaying a message to the user.
     * @return Always returns false to indicate an input error.
     */
    public boolean handleInputError() {
        out.println("Only enter numerical value");
        return false;
    }

    /**
     * @brief Attempts to parse the given string into an integer.
     * @param value The string to be parsed.
     * @return The parsed integer if successful, or -1 if parsing fails.
     */
    private int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * @brief Waits for user input to continue the program.
     * @details Displays a message prompting the user to press any key to continue.
     */
    private void enterToContinue() {
        out.println("Press any key to continue...");
        scanner.nextLine();
    }

    /**
     * @brief Loads books from a file and returns a list of Book objects.
     * @details Reads book information from the specified file and populates a List
     *          of Book objects.
     * @param pathFileBooks The path to the file containing book information.
     * @return List of Book objects representing the books in the library.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public List<Book> loadBooks(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = new ArrayList<Book>();

        // Checks if file path exists
        if (new File(pathFileBooks).exists()) {
            // Read books from file
            try (DataInputStream reader = new DataInputStream(new FileInputStream(pathFileBooks))) {
                while (reader.available() > 0) {
                    Book book = new Book();
                    book.setId(reader.readInt());
                    book.setName(reader.readUTF());
                    book.setMarked(reader.readBoolean());
                    book.setWishlist(reader.readBoolean());
                    book.setLoaned(reader.readBoolean());

                    books.add(book);
                }
            }
        }

        return books;
    }

    /**
     * @brief Writes all books to the console.
     * @details Displays information about each book in the library to the console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if books are found and displayed, false if no books are
     *         available.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            isFound = true;
            String readStatus = book.isMarked() ? "Read" : "Unread";
            String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

            out.println(
                    String.format("%d. %s (%s : %s)", book.getId(), book.getName(), readStatus, wishlistStatus));
        }

        if (!isFound) {
            out.println("There are no books.");
            return false;
        }
        return true;
    }

    /**
     * @brief Writes borrowed books to the console.
     * @details Displays information about each borrowed book in the library to the
     *          console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if borrowed books are found and displayed, false if no books are
     *         borrowed.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeBorrowedBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            if (book.isLoaned()) {
                isFound = true;
                String readStatus = book.isMarked() ? "Read" : "Unread";
                String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

                out.println(
                        String.format("%d. %s (%s : %s)", book.getId(), book.getName(), readStatus, wishlistStatus));
            }
        }

        if (!isFound) {
            out.println("There are no books to give back.");
            return false;
        }

        return true;
    }

    /**
     * @brief Writes unborrowed books to the console.
     * @details Displays information about each unborrowed book in the library to
     *          the console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if unborrowed books are found and displayed, false if all books
     *         are borrowed.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeUnBorrowedBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            if (!book.isLoaned()) {
                isFound = true;
                String readStatus = book.isMarked() ? "Read" : "Unread";
                String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

                out.println(
                        String.format("%d. %s (%s : %s)", book.getId(), book.getName(), readStatus, wishlistStatus));
            }
        }

        if (!isFound) {
            out.println("There are no books to borrow.");
            return false;
        }

        return true;
    }

}
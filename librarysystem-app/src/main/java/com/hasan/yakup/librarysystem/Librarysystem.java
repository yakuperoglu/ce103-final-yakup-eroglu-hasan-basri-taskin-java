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

    /**
     * @brief Writes wishlisted books to the console.
     * @details Displays information about each wishlisted book in the library to
     *          the console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if wishlisted books are found and displayed, false if no books
     *         are wishlisted.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeWishlistedBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            if (book.isWishlist()) {
                isFound = true;
                String readStatus = book.isMarked() ? "Read" : "Unread";
                String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

                out.println(
                        String.format("%d. %s (%s : %s)", book.getId(), book.getName(), readStatus, wishlistStatus));
            }
        }

        if (!isFound) {
            out.println("You bought all the books on your wish list.");
            return false;
        }

        return true;
    }

    /**
     * @brief Writes unwishlisted books to the console.
     * @details Displays information about each unwishlisted book in the library to
     *          the console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if unwishlisted books are found and displayed, false if all
     *         books are wishlisted.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeUnWishlistedBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            if (!book.isWishlist()) {
                isFound = true;
                String readStatus = book.isMarked() ? "Read" : "Unread";
                String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

                out.println(
                        String.format("%d. %s (%s : %s)", book.getId(), book.getName(), readStatus, wishlistStatus));
            }
        }

        if (!isFound) {
            out.println("All books are on the wish list.");
            return false;
        }

        return true;
    }

    /**
     * @brief Writes unmarked books to the console.
     * @details Displays information about each unmarked book in the library to the
     *          console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if unmarked books are found and displayed, false if all books
     *         are marked.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeUnMarkedBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            if (!book.isMarked()) {
                isFound = true;
                String readStatus = book.isMarked() ? "Read" : "Unread";
                String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

                out.println(
                        book.getId() + ". " + book.getName() + " (" + readStatus + " : " + wishlistStatus + ")");
            }
        }

        if (!isFound) {
            out.println("There are no unmarked books.");
            return false;
        }
        return true;
    }

    /**
     * @brief Writes wishlisted books to the console.
     * @details Displays information about each wishlisted book in the library to
     *          the console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if wishlisted books are found and displayed, false if no books
     *         are wishlisted.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeWishlistedBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            if (book.isWishlist()) {
                isFound = true;
                String readStatus = book.isMarked() ? "Read" : "Unread";
                String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

                out.println(
                        String.format("%d. %s (%s : %s)", book.getId(), book.getName(), readStatus, wishlistStatus));
            }
        }

        if (!isFound) {
            out.println("You bought all the books on your wish list.");
            return false;
        }

        return true;
    }

    /**
     * @brief Writes unwishlisted books to the console.
     * @details Displays information about each unwishlisted book in the library to
     *          the console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if unwishlisted books are found and displayed, false if all
     *         books are wishlisted.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeUnWishlistedBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            if (!book.isWishlist()) {
                isFound = true;
                String readStatus = book.isMarked() ? "Read" : "Unread";
                String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

                out.println(
                        String.format("%d. %s (%s : %s)", book.getId(), book.getName(), readStatus, wishlistStatus));
            }
        }

        if (!isFound) {
            out.println("All books are on the wish list.");
            return false;
        }

        return true;
    }

    /**
     * @brief Writes unmarked books to the console.
     * @details Displays information about each unmarked book in the library to the
     *          console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if unmarked books are found and displayed, false if all books
     *         are marked.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeUnMarkedBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            if (!book.isMarked()) {
                isFound = true;
                String readStatus = book.isMarked() ? "Read" : "Unread";
                String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

                out.println(
                        book.getId() + ". " + book.getName() + " (" + readStatus + " : " + wishlistStatus + ")");
            }
        }

        if (!isFound) {
            out.println("There are no unmarked books.");
            return false;
        }
        return true;
    }

    /**
     * @brief Writes marked books to the console.
     * @details Displays information about each marked book in the library to the
     *          console.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if marked books are found and displayed, false if no books are
     *         marked.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean writeMarkedBooksToConsole(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        for (Book book : books) {
            if (book.isMarked()) {
                isFound = true;
                String readStatus = book.isMarked() ? "Read" : "Unread";
                String wishlistStatus = book.isWishlist() ? "Wishlist" : "UnWishlisted";

                out.println(
                        book.getId() + ". " + book.getName() + " (" + readStatus + " : " + wishlistStatus + ")");
            }
        }

        if (!isFound) {
            out.println("There are no marked books.");
            return false;
        }
        return true;
    }

    /**
     * @brief Displays a menu to add a new book.
     * @details Clears the screen, prompts the user to enter a book name, and adds
     *          the book to the library.
     * @param pathFileBooks The path to the file containing book information.
     * @return Always returns true to indicate successful execution.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    public boolean addBookMenu(String pathFileBooks) throws InterruptedException, IOException {
        clearScreen();
        out.print("Enter a book name: ");
        String bookName = scanner.nextLine();
        addBook(bookName, pathFileBooks);
        return true;
    }

    /**
     * @brief Adds a new book to the library.
     * @details Creates a new Book object with a unique ID, sets its properties, and
     *          writes it to the file.
     * @param bookName      The name of the book to be added.
     * @param pathFileBooks The path to the file containing book information.
     * @return Always returns true to indicate successful execution.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean addBook(String bookName, String pathFileBooks) throws FileNotFoundException, IOException {
        Book newBook = new Book();
        newBook.setId(getNewId(pathFileBooks));
        newBook.setName(bookName);
        newBook.setMarked(false);
        newBook.setWishlist(false);
        newBook.setLoaned(false);

        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(pathFileBooks, true))) {
            writer.writeInt(newBook.getId());
            writer.writeUTF(newBook.getName());
            writer.writeBoolean(newBook.isMarked());
            writer.writeBoolean(newBook.isWishlist());
            writer.writeBoolean(newBook.isLoaned());
        }

        return true;
    }

    /**
     * @brief Displays a menu to delete a book.
     * @details Clears the screen, lists all books, prompts the user to enter a book
     *          number, and deletes the selected book.
     * @param pathFileBooks The path to the file containing book information.
     * @return Always returns true to indicate successful execution.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     * @throws InterruptedException  If the thread is interrupted while waiting.
     */
    public boolean deleteBookMenu(String pathFileBooks)
            throws FileNotFoundException, IOException, InterruptedException {
        clearScreen();
        writeBooksToConsole(pathFileBooks);
        out.print("Enter a number to delete book: ");

        int bookId = tryParseInt(scanner.nextLine());

        if (bookId == -1) {
            handleInputError();
            enterToContinue();
            return false;
        }

        // Delete the book with the specified ID.
        deleteBook(bookId, pathFileBooks);
        return true;
    }

    /**
     * @brief Deletes a book from the library.
     * @details Loads the books, searches for the book with the specified ID, and
     *          removes it from the file.
     * @param bookId        The ID of the book to be deleted.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if the book is found and deleted, false otherwise.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean deleteBook(int bookId, String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(new File(pathFileBooks)))) {
            for (Book book : books) {
                if (book.getId() != bookId) {
                    writer.writeInt(book.getId());
                    writer.writeUTF(book.getName());
                    writer.writeBoolean(book.isMarked());
                    writer.writeBoolean(book.isWishlist());
                    writer.writeBoolean(book.isLoaned());
                } else {
                    isFound = true;
                }
            }
        }

        if (isFound) {
            out.println("Book with ID '" + bookId + "' has been deleted successfully.");
            enterToContinue();
            return true;
        }

        out.println("There is no book you want!");
        enterToContinue();
        return false;
    }

    /**
     * @brief Displays a menu to update a book.
     * @details Clears the screen, lists all books, prompts the user to enter a book
     *          number, and updates the selected book's name.
     * @param pathFileBooks The path to the file containing book information.
     * @return Always returns true to indicate successful execution.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public boolean updateBookMenu(String pathFileBooks) throws IOException, InterruptedException {
        clearScreen();
        writeBooksToConsole(pathFileBooks);
        out.print("Enter a number to update book: ");

        int bookId = tryParseInt(scanner.nextLine());

        if (bookId == -1) {
            handleInputError();
            enterToContinue();
            return false;
        }

        out.print("Enter the new name for the book: ");
        String newBookName = scanner.nextLine();

        updateBook(bookId, newBookName, pathFileBooks);
        return true;
    }

    /**
     * @brief Updates the name of a book in the library.
     * @details Loads the books, searches for the book with the specified ID, and
     *          updates its name in the file.
     * @param bookId        The ID of the book to be updated.
     * @param newBookName   The new name for the book.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if the book is found and updated, false otherwise.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean updateBook(int bookId, String newBookName, String pathFileBooks)
            throws FileNotFoundException, IOException {
        List<Book> books = loadBooks(pathFileBooks);
        boolean isFound = false;

        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(new File(pathFileBooks)))) {
            for (Book book : books) {
                writer.writeInt(book.getId());
                if (book.getId() != bookId) {
                    writer.writeUTF(book.getName());
                } else {
                    writer.writeUTF(newBookName);
                    isFound = true;
                }
                writer.writeBoolean(book.isMarked());
                writer.writeBoolean(book.isWishlist());
                writer.writeBoolean(book.isLoaned());
            }
        }

        if (isFound) {
            out.println("Book with ID '" + bookId + "' has been updated successfully.");
            enterToContinue();
            return true;
        }

        out.println("There is no book with the specified ID.");
        enterToContinue();
        return false;
    }

    /**
     * @brief Displays the book catalog to the console.
     * @details Clears the screen, writes the book catalog to the console, and
     *          prompts the user to continue.
     * @param filePathBooks The path to the file containing book information.
     * @return Always returns true to indicate successful execution.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    public boolean viewCatalog(String filePathBooks) throws InterruptedException, IOException {
        clearScreen();
        writeBooksToConsole(filePathBooks);
        enterToContinue();
        return true;
    }

    /**
     * @brief Displays the book cataloging menu to the console.
     * @details Clears the screen and prints the book cataloging menu options.
     * @return Always returns true to indicate successful execution.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    public boolean bookCatalogingMenu() throws InterruptedException, IOException {
        clearScreen();
        out.println("Welcome to Book Operations\n\n");
        out.println("1. Add Book");
        out.println("2. Delete Book");
        out.println("3. Update Book");
        out.println("4. View Catalog");
        out.println("5. Return User Operations");
        out.println("Please enter a number to select:");

        return true;
    }

    /**
     * @brief Manages book cataloging operations.
     * @details Displays the book cataloging menu, processes the user's choice, and
     *          executes the corresponding operation.
     * @param pathFileBooks The path to the file containing book information.
     * @return Always returns true to indicate successful execution.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    public boolean bookCataloging(String pathFileBooks) throws InterruptedException, IOException {
        int choice;
        while (true) {
            bookCatalogingMenu();

            choice = tryParseInt(scanner.nextLine());

            if (choice == -1) {
                handleInputError();
                enterToContinue();
                continue;
            }

            switch (choice) {
                case 1:
                    addBookMenu(pathFileBooks);
                    break;

                case 2:
                    deleteBookMenu(pathFileBooks);
                    break;

                case 3:
                    updateBookMenu(pathFileBooks);
                    break;

                case 4:
                    viewCatalog(pathFileBooks);
                    break;

                case 5:
                    return false;

                default:
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }

    /**
     * @brief Authenticates a user by checking the email and password against
     *        existing user data.
     * @details Reads user data from the specified file and compares it with the
     *          provided user credentials.
     * @param user          The user to be authenticated.
     * @param pathFileUsers The path to the file containing user information.
     * @return True if the login is successful, false otherwise.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public boolean loginUser(User user, String pathFileUsers) throws FileNotFoundException, IOException {
        if (new File(pathFileUsers).exists()) {
            try (DataInputStream reader = new DataInputStream(new FileInputStream(pathFileUsers))) {
                while (reader.available() > 0) {
                    User existingUser = new User();
                    existingUser.setEmail(reader.readUTF());
                    existingUser.setPassword(reader.readUTF());

                    if (existingUser.getEmail().equals(user.getEmail())
                            && existingUser.getPassword().equals(user.getPassword())) {
                        out.println("Login successful.");
                        enterToContinue();
                        return true;
                    }
                }
            }
        }
        out.println("Invalid email or password. Please try again.");
        enterToContinue();
        return false;
    }

    /**
     * @brief Displays a menu for user login and authenticates the user.
     * @details Prompts the user to enter email and password, and attempts to
     *          authenticate the user.
     * @param pathFileUsers The path to the file containing user information.
     * @return Always returns true to indicate successful execution.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    public boolean loginUserMenu(String pathFileUsers) throws InterruptedException, IOException {
        clearScreen();
        User loginUser = new User();

        out.print("Enter email: ");
        loginUser.setEmail(scanner.nextLine());

        out.print("Enter password: ");
        loginUser.setPassword(scanner.nextLine());

        return loginUser(loginUser, pathFileUsers);
    }

    /**
     * @brief Displays the loan management menu to the console.
     * @details Clears the screen and prints the loan management menu options.
     * @return Always returns true to indicate successful execution.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    private boolean loanManagementMenu() throws InterruptedException, IOException {
        clearScreen();
        out.println("Loan Management Menu\n\n");
        out.println("1. Give Book");
        out.println("2. Borrow Book");
        out.println("3. View Borrowed Books");
        out.println("4. Return to User Operations Menu");
        out.println("Please enter a number to select:");
        return true;
    }

    /**
     * @brief Displays the menu for giving back a borrowed book.
     * @details Clears the screen, writes the list of borrowed books to the console,
     *          and prompts the user to enter the ID of the book to give back.
     * @param pathFileBooks The path to the file containing book information.
     * @return True if the book is given back successfully, false otherwise.
     * @throws FileNotFoundException If the specified file is not found.
     * @throws IOException           If an I/O error occurs.
     * @throws InterruptedException  If the thread is interrupted while waiting.
     */
    public boolean giveBookMenu(String pathFileBooks) throws FileNotFoundException, IOException, InterruptedException {
        clearScreen();
        writeBorrowedBooksToConsole(pathFileBooks);

        out.print("Enter the ID of the book you want to give back: ");

        int bookId = tryParseInt(scanner.nextLine());

        if (bookId == -1) {
            handleInputError();
            enterToContinue();
            return false;
        }

        return giveBook(bookId, pathFileBooks);
    }
    
    
/**
 * @brief Gives back a borrowed book.
 * @details Loads the books, searches for the book with the specified ID, updates its loan status, and writes the changes to the file.
 * @param bookId The ID of the book to be given back.
 * @param pathFileBooks The path to the file containing book information.
 * @return True if the book is given back successfully, false otherwise.
 * @throws FileNotFoundException If the specified file is not found.
 * @throws IOException If an I/O error occurs.
 */
public boolean giveBook(int bookId, String pathFileBooks) throws FileNotFoundException, IOException {
    List<Book> books = loadBooks(pathFileBooks);
    boolean isFound = false;

    try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(pathFileBooks))) {
        for (Book book : books) {
            writer.writeInt(book.getId());
            writer.writeUTF(book.getName());
            writer.writeBoolean(book.isMarked());
            writer.writeBoolean(book.isWishlist());

            if (book.getId() == bookId && book.isLoaned()) {
                writer.writeBoolean(false);
                isFound = true;
            } else {
                writer.writeBoolean(book.isLoaned());
            }
        }
    }

    if (isFound) {
        out.println("Book returned successfully.");
        enterToContinue();
        return true;
    }

    out.println("There is no book you want!");
    enterToContinue();
    return false;
}

/**
* @brief Displays the menu for borrowing a book.
* @details Clears the screen, writes the list of available books to the console, and prompts the user to enter the ID of the book to borrow.
* @param pathFileBooks The path to the file containing book information.
* @return True if the book is borrowed successfully, false otherwise.
* @throws FileNotFoundException If the specified file is not found.
* @throws IOException If an I/O error occurs.
* @throws InterruptedException If the thread is interrupted while waiting.
*/
public boolean borrowBookMenu(String pathFileBooks)
        throws FileNotFoundException, IOException, InterruptedException {
    clearScreen();
    writeUnBorrowedBooksToConsole(pathFileBooks);

    out.print("Enter the ID of the book you want to borrow: ");

    int bookId = tryParseInt(scanner.nextLine());

    if (bookId == -1) {
        handleInputError();
        enterToContinue();
        return false;
    }

    return borrowBook(bookId, pathFileBooks);
}

/**
* @brief Borrows a book.
* @details Loads the books, searches for the book with the specified ID, updates its loan status, and writes the changes to the file.
* @param bookId The ID of the book to be borrowed.
* @param pathFileBooks The path to the file containing book information.
* @return True if the book is borrowed successfully, false otherwise.
* @throws FileNotFoundException If the specified file is not found.
* @throws IOException If an I/O error occurs.
*/
public boolean borrowBook(int bookId, String pathFileBooks) throws FileNotFoundException, IOException {
    List<Book> books = loadBooks(pathFileBooks);
    boolean isFound = false;

    try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(pathFileBooks))) {
        for (Book book : books) {
            writer.writeInt(book.getId());
            writer.writeUTF(book.getName());
            writer.writeBoolean(book.isMarked());
            writer.writeBoolean(book.isWishlist());

            if (book.getId() == bookId && !book.isLoaned()) {
                writer.writeBoolean(true);
                isFound = true;
            } else {
                writer.writeBoolean(book.isLoaned());
            }
        }
    }

    if (isFound) {
        out.println("Book borrowed successfully.");
        enterToContinue();
        return true;
    }

    out.println("There is no book you want!");
    enterToContinue();
    return false;
}


}
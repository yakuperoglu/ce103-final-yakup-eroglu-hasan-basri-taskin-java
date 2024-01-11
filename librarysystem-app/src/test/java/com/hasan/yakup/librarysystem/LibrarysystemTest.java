package com.hasan.yakup.librarysystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

public class LibrarysystemTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private String testFilePathBooks = "test_books.bin";
    private String testFilePathUsers = "test_users.bin";

    @After
    public void tearDown() throws IOException {
        cleanupTestDataBook();
        cleanupTestDataUser();
        System.setOut(null);
        System.setIn(null);
    }

    @Test
    public void testAddBookMenu_SuccessfulAddition() throws InterruptedException, IOException {
        String input = "NewBook\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.addBookMenu(testFilePathBooks);

        System.setIn(System.in);

        assertTrue(result);
    }

    @Test
    public void testGetNewId_ShouldReturnCorrectId() throws FileNotFoundException, IOException {

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));
        int newId = library.getNewId(testFilePathBooks);

        assertEquals(1, newId);
    }

    @Test
    public void testLoadBooks_ShouldLoadBooksFromFile() throws FileNotFoundException, IOException {
        createTestFile();

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));
        List<Book> books = library.loadBooks(testFilePathBooks);

        assertNotNull(books);
        assertEquals(4, books.size());
    }

    @Test
    public void testHandleInputError_ShouldReturnFalse() {
        InputStream in = new ByteArrayInputStream("".getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.handleInputError();

        assertFalse(result);
    }

    @Test
    public void testMain_Menu_ShouldVisitAllFunctionsAndExit() throws InterruptedException, IOException {
        // String input = "2\nexample\nexample\n\n3\n2\n1\nexample\nexample\n\n5\n4";
        String input = "2\nexample\nexample\n\n1\nexample\nexample\n\n5\n3\n2\n123\n\n4\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        int result = library.mainMenu(testFilePathUsers, testFilePathBooks);
        assertEquals(0, result);
    }

    @Test
    public void testMain_Menu_DefaultChoice_ShouldPrintInvalidChoiceAndExit() throws InterruptedException, IOException {
        String input = "abc\n1231231234\n4";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        int result = library.mainMenu(testFilePathUsers, testFilePathBooks);
        assertEquals(0, result);
    }

    @Test
    public void testRegisterUser_ShouldRegisterUser() throws InterruptedException, IOException {
        String input = "\ntest@example.com\ntestpassword\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.registerMenu(testFilePathUsers);

        assertTrue(result);
    }

    @Test
    public void testLoginUser_ShouldLoginUserSuccessfully()
            throws FileNotFoundException, IOException, InterruptedException {

        User testUser = new User();
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("123");

        String inputString = "\ntest@gmail.com\n123\n\n";

        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        library.registerUser(testUser, testFilePathUsers);

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        System.setIn(new ByteArrayInputStream(inputString.getBytes()));

        boolean result = library.loginUserMenu(testFilePathUsers);

        assertTrue(result);
    }

    @Test
    public void testLoginUser_InvalidInput_ShouldPrintErrorMessage()
            throws FileNotFoundException, IOException, InterruptedException {

        User testUser = new User();
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("123");
        String inputString = "\ninvalidemail\ninvalidpassword\n\n";

        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));
        library.registerUser(testUser, testFilePathUsers);

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        System.setIn(new ByteArrayInputStream(inputString.getBytes()));

        boolean result = library.loginUserMenu(testFilePathUsers);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testViewCatalog_ShouldDisplayBooks() throws IOException,
            InterruptedException {

        createTestFile();

        InputStream in = new ByteArrayInputStream("\n".getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.viewCatalog(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testGuestOperation_ShouldDisplayMenuAndExit() throws InterruptedException, IOException {
        String inputString = "abc\n\n321312\n\n1\n\n2\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        int result = library.guestOperation(testFilePathUsers);

        assertEquals(result, 0);
    }

    @Test
    public void testDeleteBookMenu_SuccessfulDeletion() throws IOException,
            InterruptedException {
        createTestFile();

        String inputString = "2\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.deleteBookMenu(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testDeleteBookMenu_ShouldInvalidInput() throws IOException,
            InterruptedException {

        createTestFile();

        String inputString = "qwe\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.deleteBookMenu(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testDeleteBook_BookNotFound() throws IOException,
            InterruptedException {

        createTestFile();
        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.deleteBook(5, testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testUserOperations_ReturnsFalseOnExit() throws InterruptedException, IOException {
        String inputString = "qwe\n\n1233\n\n1\n5\n2\n4\n3\n4\n4\n4\n5\n";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));
        ;

        boolean result = library.userOperations(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testBookCataloging_ReturnsFalseOnReturnToUserOperations() throws InterruptedException, IOException {

        // String inputString = "qwe\n1\nqwe\n2\n1\n1\nqwe\n3\n1\nqwe1\n4\n321\n5\n";
        String inputString = "qwe\n\n321\n\n1\nBookName\n2\n1\n\n3\n1\nqwe\n\n4\n\n5\n";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.bookCataloging(testFilePathBooks);

        assertFalse(result);

    }

    @Test
    public void testLoanManagement_ReturnsFalseOnReturnToUserOperations() throws IOException, InterruptedException {

        createTestFile();

        String inputString = "qwe\n\n321\n\n2\n1\n\n1\n1\n\n3\n\n4\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.loanManagement(testFilePathBooks);

        assertFalse(result);
    }

}
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

    @Test
    public void testUpdateBookMenu_ShouldUpdateBookSuccessfully() throws IOException, InterruptedException {

        createTestFile();

        String inputString = "2\nUpdatedBook\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.updateBookMenu(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testUpdateBookMenu_InvalidInput_ShouldPrintErrorMessage() throws IOException, InterruptedException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        String inputString = "qwe\n\n132\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.updateBookMenu(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testUpdateBookMenu_InvalidBookId_ShouldReturnFalse() throws IOException, InterruptedException {

        createTestFile();

        String inputString = "\n123132";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.updateBookMenu(testFilePathBooks);

        assertFalse(result); // Sadece boolean kontrolü yapılacak.

        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setIn(System.in);
    }

    @Test
    public void testWriteUnBorrowedBooksToConsole_ShouldReturnTrue() throws IOException, InterruptedException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));
        boolean result = library.writeUnBorrowedBooksToConsole(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testWriteUnBorrowedBooksToConsole_ShouldntFindBooks() throws FileNotFoundException, IOException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));
        boolean result = library.writeUnBorrowedBooksToConsole(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testWriteBorrowedBooksToConsole_ShouldWriteBooksAndReturnTrue()
            throws IOException, InterruptedException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));
        boolean result = library.writeBorrowedBooksToConsole(testFilePathBooks);

        assertTrue(result);

        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setIn(System.in);
    }

    @Test
    public void testWriteBorrowedBooksToConsole_ShouldntFindBooks() throws FileNotFoundException, IOException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));
        boolean result = library.writeBorrowedBooksToConsole(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testViewBorrowedBooks_ShouldDisplayBooksAndReturnTrue()
            throws FileNotFoundException, IOException, InterruptedException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));
        boolean result = library.viewBorrowedBooks(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testBorrowBookMenu_ShouldBorrowBookAndReturnTrue()
            throws FileNotFoundException, IOException, InterruptedException {

        createTestFile();

        String inputString = "1\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.borrowBookMenu(testFilePathBooks);

        assertTrue(result);

    }

    @Test
    public void testBorrowBookMenu_ShouldNotBorrowBookAndReturnFalse() throws IOException, InterruptedException {

        createTestFile();

        String inputString = "2\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.borrowBookMenu(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testBorrowBookMenu_InvalidInputShouldReturnFalse()
            throws FileNotFoundException, IOException, InterruptedException {

        String inputString = "qwe\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.borrowBookMenu(testFilePathBooks);

        assertFalse(result);

    }

    @Test
    public void testGiveBookMenu_InvalidInput() throws FileNotFoundException,
            IOException, InterruptedException {

        String inputString = "qwe\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.giveBookMenu(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testGiveBookMenu_ShouldGiveBookBackAndReturnTrue()
            throws FileNotFoundException, IOException, InterruptedException {

        createTestFile();

        String inputString = "2\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.giveBookMenu(testFilePathBooks);

        assertTrue(result);

    }

    @Test
    public void testGiveBookMenu_ShouldNotGiveBookBackAndReturnFalse()
            throws FileNotFoundException, IOException, InterruptedException {

        createTestFile();

        String inputString = "1\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.giveBookMenu(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testGiveBook_ShouldGiveBookBackAndReturnTrue() throws IOException {

        createTestFile();

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));
        boolean result = library.giveBook(2, testFilePathBooks);

        assertTrue(result);

    }

    @Test
    public void testGiveBook_ShouldNotGiveBookBackAndReturnFalse() throws IOException {

        createTestFile();

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));
        boolean result = library.giveBook(1, testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testWishList_InvalidOption() throws InterruptedException,
            IOException {

        String inputString = "312\n\nqwe\n\n1\n\n2\n1\n\n3\n1\n\n4\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.wishList(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testListWishList_ShouldWriteBooksAndReturnTrue() throws IOException, InterruptedException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));

        boolean result = library.listWishList(testFilePathBooks);

        assertTrue(result);

    }

    @Test
    public void testRemoveFromWishListMenu_InputError()
            throws FileNotFoundException, IOException, InterruptedException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        String inputString = "qwe\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.removeFromWishListMenu(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testRemoveFromWishListMenu_ShouldRemoveBookAndReturnTrue() throws IOException, InterruptedException {

        String inputString = "2\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        createTestFile();

        boolean result = library.removeFromWishListMenu(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void addBook_ShouldAddBookToList() throws FileNotFoundException,
            IOException {

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));
        boolean result = library.addBook("Test Book", testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testRemoveFromWishListMenu_InvalidBookId() throws IOException,
            InterruptedException {

        String inputString = "8448\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        createTestFile();

        boolean result = library.removeFromWishListMenu(testFilePathBooks);

        assertFalse(result);

    }

    @Test
    public void testAddToWishListMenu_InputError() throws FileNotFoundException,
            IOException, InterruptedException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        String inputString = "qwe\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.addToWishListMenu(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testAddToWishListMenu_ShouldAddBookAndReturnTrue() throws IOException, InterruptedException {

        createTestFile();

        String inputString = "4\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.addToWishListMenu(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testAddToWishListMenu_InvalidBookId() throws IOException,
            InterruptedException {

        String inputString = "89\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        createTestFile();

        boolean result = library.addToWishListMenu(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testWishList_ReturnsFalseOnReturnToUserOperations() throws InterruptedException, IOException {

        String inputString = "qwe\n1\n\n2\n1\n\n3\n\n1\n4";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.wishList(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testWriteBooksToConsole_NoBooks() throws FileNotFoundException,
            IOException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));

        boolean result = library.writeBooksToConsole(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testWriteBooksToConsole_ShouldWriteBooks() throws FileNotFoundException, IOException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));

        boolean result = library.writeBooksToConsole(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testWriteMarkedBooksToConsole_NoMarkedBooks() throws FileNotFoundException, IOException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));

        boolean result = library.writeMarkedBooksToConsole(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testWriteMarkedBooksToConsole_ShouldWriteMarkedBooks() throws FileNotFoundException, IOException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));

        boolean result = library.writeMarkedBooksToConsole(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testWriteUnMarkedBooksToConsole_NoUnmarkedBooks() throws FileNotFoundException, IOException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));

        boolean result = library.writeUnMarkedBooksToConsole(testFilePathBooks);

        assertFalse(result);

    }

    @Test
    public void testWriteUnMarkedBooksToConsole_ShouldWriteUnmarkedBooks() throws FileNotFoundException, IOException {
        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));

        boolean result = library.writeUnMarkedBooksToConsole(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testLogProgress_ShouldWriteBooksAndReturnTrue() throws InterruptedException, IOException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));

        boolean result = library.logProgress(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testLogProgress_ShouldntWriteBooksAndReturnFalse() throws InterruptedException, IOException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));

        boolean result = library.logProgress(testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testMarkAsReadMenu_InputError() throws FileNotFoundException,
            IOException, InterruptedException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        String inputString = "qwe\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.markAsReadMenu(testFilePathBooks);

        assertFalse(result);

    }

    @Test
    public void testMarkAsReadMenu_ShouldWriteMarkedBooksAndReturnTrue()
            throws FileNotFoundException, IOException, InterruptedException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        String inputString = "4\n\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.markAsReadMenu(testFilePathBooks);

        assertTrue(result);

    }

    @Test
    public void testMarkAsRead_ShouldUpdateBookAndReturnTrue() throws IOException {

        createTestFile();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));

        boolean result = library.markAsRead(1, testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testMarkAsRead_ShouldntUpdateBookAndReturnFalse() throws FileNotFoundException, IOException {

        createTestFile();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));

        boolean result = library.markAsRead(2, testFilePathBooks);

        assertFalse(result);
    }

    @Test
    public void testViewHistory_ShouldWriteMarkedBooksToConsole() throws InterruptedException, IOException {

        createTestFile();

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));
        boolean result = library.viewHistory(testFilePathBooks);

        assertTrue(result);
    }

    @Test
    public void testViewHistory_ShouldntWriteMarkedBooksToConsole() throws InterruptedException, IOException {

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        Librarysystem library = new Librarysystem(new Scanner("\n"), new PrintStream(outContent));
        boolean result = library.viewHistory(testFilePathBooks);

        assertFalse(result);

    }

    @Test
    public void testReadingTracker_ReturnsFalseOnReturnToUserOperations() throws InterruptedException, IOException {

        createTestFile();

        String inputString = "qwe\n\n321\n\n1\n\n2\n1\n3\n\n4\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        Scanner testScanner = new Scanner(in);
        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        boolean result = library.readingTracker(testFilePathBooks);

        assertFalse(result);
    }

    private void createTestFile() throws IOException {
        // Kitaplar
        List<Book> testBooks = new ArrayList<>();
        testBooks.add(new Book(1, "Book1", false, false, false));
        testBooks.add(new Book(2, "Book2", true, true, true));
        testBooks.add(new Book(3, "Book3", true, true, false));
        testBooks.add(new Book(4, "Book4", false, false, true));

        // Test dosyasını kitaplarla birlikte oluştur
        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(testFilePathBooks))) {
            for (Book book : testBooks) {
                writer.writeInt(book.getId());
                writer.writeUTF(book.getName());
                writer.writeBoolean(book.isMarked());
                writer.writeBoolean(book.isWishlist());
                writer.writeBoolean(book.isLoaned());
            }
        }
    }

    private void cleanupTestDataBook() throws IOException {
        deleteFile(testFilePathBooks);
    }

    private void cleanupTestDataUser() throws IOException {
        deleteFile(testFilePathUsers);
    }

    private void deleteFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }

}
